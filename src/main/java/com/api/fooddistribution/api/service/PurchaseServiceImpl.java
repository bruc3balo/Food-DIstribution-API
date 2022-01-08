package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DistributionUpdateForm;
import com.api.fooddistribution.api.model.PurchaseCreationForm;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.utils.DataOps;
import com.api.fooddistribution.utils.DistributionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.config.FirestoreConfig.firebaseDatabase;
import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.productService;
import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.utils.DataOps.getDistributionModelFromDistribution;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedFullDate;
import static java.lang.String.valueOf;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Override
    public Models.Purchase createNewPurchase(PurchaseCreationForm form) throws NotFoundException, ParseException {

        Long id = getNextPurchaseId();

        Optional<Models.AppUser> buyer = userService.findByUsername(form.getBuyerId());

        if (buyer.isEmpty()) {
            throw new NotFoundException("Buyer has not been found");
        }

        final boolean[] throwE = {false};
        LinkedHashMap<String, Integer> products = new LinkedHashMap<>();
        try {
            System.out.println("Products are " + new ObjectMapper().writeValueAsString(form.getProduct()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        form.getProduct().forEach((productId, items) -> {
            Optional<Models.Product> optionalProduct = productService.findProductById(productId);
            if (optionalProduct.isEmpty()) {
                throwE[0] = true;
            }


            optionalProduct.ifPresent(product -> {
                products.put(product.getId(), items);
                System.out.println(product.getName() + " has been added");
            });

        });

        if (throwE[0]) {
            throw new NotFoundException("Product has not been found");
        }

        Models.Purchase purchase = new Models.Purchase(id, form.getLocation(), form.getAddress(), buyer.get().getUsername(), getNowFormattedFullDate(), products);
        Models.Purchase savedPurchase = purchaseRepo.save(purchase);

        if (savedPurchase != null) {
            //delete cart
            firebaseDatabase.getReference("Cart").child(buyer.get().getUid()).removeValue((databaseError, databaseReference) -> System.out.println("Cart Cleared for " + buyer.get().getUsername()));
            //todo notify sellers

            //todo find transporters
        }

        return savedPurchase;
    }

    @Override
    public List<Models.Purchase> getPurchases(String buyerId, String sellerId) {
        return buyerId == null ? sellerId == null ? purchaseRepo.retrieveAll() : purchaseRepo.retrieveAll().stream().filter(i -> i.getProducts().keySet().stream().map(id -> productService.findProductById(id).get().getSellerId()).collect(Collectors.toList()).contains(sellerId)).collect(Collectors.toList()) : purchaseRepo.retrieveAll().stream().filter(i -> i.getBuyerId().equals(buyerId)).collect(Collectors.toList());
    }

    @Override
    public Models.DistributionModel saveNewDistribution(Long purchaseId, String transporterUsername) throws NotFoundException, ParseException {
        Optional<Models.Purchase> optionalPurchase = purchaseRepo.get(valueOf(purchaseId));
        if (optionalPurchase.isEmpty()) {
            throw new NotFoundException("purchase with id " + purchaseId + " not found");
        }

        Optional<Models.AppUser> optionalTransporter = userService.findByUsername(transporterUsername);
        if (optionalTransporter.isEmpty()) {
            throw new NotFoundException("user with name " + transporterUsername + " not found");
        }

        optionalPurchase.get().setAssigned(optionalTransporter.get().getUsername());
        Models.Purchase purchase = purchaseRepo.save(optionalPurchase.get());

        if (purchase != null) {

            Optional<Models.AppUser> buyerOptional = userService.findByUsername(purchase.getBuyerId());
            if (buyerOptional.isEmpty()) {
                throw new NotFoundException("user with name " + purchase.getBuyerId() + " not found");
            }


            Map<String, Integer> productStatus = new HashMap<>();
            purchase.getProducts().keySet().forEach(pid -> productStatus.put(pid, 0));

            Models.Distribution distribution = new Models.Distribution(getNextDistributionId(), buyerOptional.get().getRole().getName().equals(AppRolesEnum.ROLE_DONOR.name()) ? buyerOptional.get().getUsername() : null,
                    optionalTransporter.get().getUsername(), buyerOptional.get().getRole().getName().equals(AppRolesEnum.ROLE_BUYER.name()) ? buyerOptional.get().getUsername() : null,
                    DistributionStatus.ACCEPTED.getCode(), getNowFormattedFullDate(), getNowFormattedFullDate(), null, purchase.getId(), optionalTransporter.get().getLastKnownLocation(), false, false, false, null, productStatus);


            return getDistributionModelFromDistribution(distributionRepo.save(distribution));
        } else {
            return null;
        }
    }

    private Long getNextDistributionId() {
        final List<Long> ids = distributionRepo.retrieveAll().stream().map(Models.Distribution::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ids.isEmpty() ? 1 : ids.get(0) + 1;
    }

    private Long getNextPurchaseId() {
        final List<Long> ids = purchaseRepo.retrieveAll().stream().map(Models.Purchase::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ids.isEmpty() ? 1 : ids.get(0) + 1;
    }

    private Long getNextRemarkId() {
        final List<Long> ids = remarksRepo.retrieveAll().stream().map(Models.Remarks::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ids.isEmpty() ? 1 : ids.get(0) + 1;
    }

    @Override
    public Optional<Models.DistributionModel> getDistributionById(Long id) {
        Optional<Models.Distribution> d = distributionRepo.get(valueOf(id));
        if (d.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getDistributionModelFromDistribution(d.orElse(null)));
    }

    @Override
    public List<Models.DistributionModel> getDistribution(String transporter, String beneficiary, String sellerId, String donor, Boolean paid, Boolean deleted, Long purchasesId, Integer status,Boolean completed) {
        List<Models.Distribution> allDistributions = distributionRepo.retrieveAll();
        List<Models.DistributionModel> allDistributionModels = new ArrayList<>();

        if (transporter != null) {
            allDistributions.removeIf(i -> !i.getTransporter().equals(transporter));
        }

        if (beneficiary != null) {
            allDistributions.removeIf(i -> !i.getBeneficiary().equals(beneficiary));
        }

        if (sellerId != null) {
            allDistributions.removeIf(i -> !getDistributionModelFromDistribution(i).getPurchases().getProducts().stream().map(m -> m.getProduct().getSellerId()).collect(Collectors.toList()).contains(sellerId));
        }

        if (donor != null) {
            allDistributions.removeIf(i -> !i.getDonor().equals(donor));
        }

        if (paid != null) {
            allDistributions.removeIf(i -> !(i.getPaid() == paid));
        }

        if (deleted != null) {
            allDistributions.removeIf(i -> !(i.getDeleted() == deleted));
        }

        if (completed != null) {
            allDistributions.removeIf(i -> completed == (i.getCompletedAt() != null));
        }

        if (purchasesId != null) {
            allDistributions.removeIf(i -> !(Objects.equals(i.getPurchasesId(), purchasesId)));
        }

        if (status != null) {
            allDistributions.removeIf(i -> !(Objects.equals(i.getStatus(), status)));
        }

        allDistributions.forEach(d-> allDistributionModels.add(getDistributionModelFromDistribution(d)));

        return allDistributionModels;
    }

    @Override
    public Models.DistributionModel updateDistribution(DistributionUpdateForm form) throws NotFoundException, ParseException {

        Optional<Models.Distribution> optionalDistribution = distributionRepo.get(valueOf(form.getId()));
        if (optionalDistribution.isEmpty()) {
            throw new NotFoundException("distribution not found");
        }

        Models.Distribution updatedDistribution = optionalDistribution.get();

        if (form.getDeleted() != null) {
            updatedDistribution.setDeleted(form.getDeleted());
        }

        if (form.getPaid() != null) {
            updatedDistribution.setPaid(form.getPaid());
        }

        if (form.getLastKnownLocation() != null) {
            updatedDistribution.setLastKnownLocation(form.getLastKnownLocation());
        }

        if (form.getRemarks() != null) {
            Optional<Models.Remarks> optionalRemarks = remarksRepo.get(valueOf(form.getRemarks()));
            if (optionalRemarks.isEmpty()) {
                throw new NotFoundException("Remark not found");
            }
            updatedDistribution.setRemarks(optionalRemarks.get().getId());
        }

        if (form.getStatus() != null) {
            updatedDistribution.setStatus(form.getStatus());

            switch (form.getStatus()) {

                //COLLECTING_ITEMS
                case 1:
                    break;

                 //ON_THE_WAY
                case 2:
                    break;

                 //ARRIVED
                case 3:
                    break;

                 //COMPLETE
                case 4:
                    updatedDistribution.setCompletedAt(DataOps.getNowFormattedFullDate());
                    Optional<Models.Purchase> optionalPurchase = purchaseRepo.get(String.valueOf(updatedDistribution.getPurchasesId()));
                    optionalPurchase.ifPresent(p-> {
                        p.setComplete(true);
                        purchaseRepo.save(p);
                    });
                    break;

                 //DNF
                case 5:
                    updatedDistribution.setCompletedAt(DataOps.getNowFormattedFullDate());
                    break;
            }
        }

        if (form.getProductStatus() != null) {
            form.getProductStatus().forEach((pid, status) -> {
                Optional<Map.Entry<String, Integer>> pStatus = updatedDistribution.getProductStatus().entrySet().stream().filter(i -> i.getKey().equals(pid)).findFirst();
                pStatus.ifPresent((pStatusObj) -> pStatusObj.setValue(status));
            });

        }

        updatedDistribution.setUpdatedAt(getNowFormattedFullDate());
        return getDistributionModelFromDistribution(distributionRepo.save(updatedDistribution));
    }

    @Override
    public Models.Remarks createNewRemark(Models.Remarks remarks) throws NotFoundException {

        Optional<Models.Distribution> optionalDistribution = distributionRepo.get(String.valueOf(remarks.getDistributionId()));

        if(optionalDistribution.isEmpty()) {
            throw new NotFoundException("distribution not found");
        }


        remarks.setId(getNextRemarkId());
        remarks.setDocumentId(String.valueOf(remarks.getId()));


        Models.Remarks savedRemark = remarksRepo.save(remarks);


        optionalDistribution.get().setRemarks(savedRemark.getId());
        distributionRepo.save(optionalDistribution.get());

        return savedRemark;
    }

    @Override
    public Models.Remarks updateRemark(Models.Remarks remarks) throws NotFoundException {

        Optional<Models.Remarks> optionalRemarks = remarksRepo.get(String.valueOf(remarks.getId()));

        if (optionalRemarks.isEmpty()) {
            throw new NotFoundException("Remarks not found");
        }

        return remarksRepo.save(remarks);
    }

    @Override
    public Optional<Models.Remarks> getRemark(Long id) {
        return remarksRepo.get(String.valueOf(id));
    }
}
