package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DistributionUpdateForm;
import com.api.fooddistribution.api.model.DonationCreationForm;
import com.api.fooddistribution.api.model.DonorDistributionUpdateForm;
import com.api.fooddistribution.api.model.PurchaseCreationForm;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.utils.DataOps;
import com.api.fooddistribution.utils.DistributionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.config.FirestoreConfig.firebaseDatabase;
import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.*;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.*;
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


        form.getProduct().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
            product.setUnitsLeft(product.getUnitsLeft() - items);
            productRepo.save(product);
        }));

        Models.Purchase purchase = new Models.Purchase(id, form.getLocation(), form.getAddress(), buyer.get().getUsername(), getNowFormattedFullDate(), products, null);
        Models.Purchase savedPurchase = purchaseRepo.save(purchase);

        if (savedPurchase != null) {
            //delete cart
            firebaseDatabase.getReference("Cart").child(buyer.get().getUid()).removeValue((databaseError, databaseReference) -> System.out.println("Cart Cleared for " + buyer.get().getUsername()));
            notificationService.postNotification(new Models.NotificationModels("You will be notified of the progress", "Your purchase request has been received", "Purchase", savedPurchase.getBuyerId(), PURCHASE_COLLECTION));
            form.getProduct().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                try {
                    notificationService.postNotification(new Models.NotificationModels("You will be notified of the progress", product.getName() + " has been purchased for " + items + " items", "Purchase", product.getSellerId(), PURCHASE_COLLECTION));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }));
            //todo find transporters
        }

        return savedPurchase;
    }

    @Override
    public List<Models.Purchase> getPurchases(String buyerId, String sellerId) {

        return purchaseRepo.retrieveAll().stream().filter(i -> {
            boolean add = true;

            if (buyerId != null) {
                add = i.getBuyerId().equals(buyerId);
            }

            if (sellerId != null) {
                final List<String> sellerNames = i.getProducts().keySet().stream().map(id -> {
                    final Optional<Models.Product> productById = productService.findProductById(id);
                    if (productById.isPresent()) {
                        return productById.get().getSellerId();
                    } else {
                        return id;
                    }
                }).collect(Collectors.toList());
                add = sellerNames.contains(sellerId);
            }

            return add;
        }).collect(Collectors.toList());

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

            Models.Distribution distribution = new Models.Distribution(getNextDistributionId(),
                    optionalTransporter.get().getUsername(), buyerOptional.get().getRole().getName().equals(AppRolesEnum.ROLE_BUYER.name()) ? buyerOptional.get().getUsername() : null,
                    DistributionStatus.ACCEPTED.getCode(), getNowFormattedFullDate(), getNowFormattedFullDate(), null, purchase.getId(), optionalTransporter.get().getLastKnownLocation(), false, false, false, null, productStatus);

            notificationService.postNotification(new Models.NotificationModels("A transporter " + (distribution.getTransporter()) + " has accepted the purchase", "Distribution created", "Distribution", distribution.getBeneficiary(), DISTRIBUTION_COLLECTION));


            purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                try {
                    notificationService.postNotification(new Models.NotificationModels("A transported " + (distribution.getTransporter()) + " has accepted the purchase is gathering the items "+(product.getPrice().multiply(new BigDecimal(items)) + " Ksh"), "Distribution created", "Distribution", product.getSellerId(), DISTRIBUTION_COLLECTION));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }));

            return getDistributionModelFromDistribution(distributionRepo.save(distribution));
        } else {
            return null;
        }
    }

    private Long getNextDistributionId() {
        final List<Long> ids = distributionRepo.retrieveAll().stream().map(Models.Distribution::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ids.isEmpty() ? 1 : ids.get(0) + 1;
    }

    private Long getNextDonationDistributionId() {
        final List<Long> ids = donationDistributionRepo.retrieveAll().stream().map(Models.DonationDistribution::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ids.isEmpty() ? 1 : ids.get(0) + 1;
    }

    private Long getNextDonationId() {
        final List<Long> ids = donationRepo.retrieveAll().stream().map(Models.Donation::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
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
    public List<Models.DistributionModel> getDistribution(String transporter, String beneficiary, String sellerId, Boolean paid, Boolean deleted, Long purchasesId, Integer status, Boolean completed) {
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

        allDistributions.forEach(d -> allDistributionModels.add(getDistributionModelFromDistribution(d)));

        return allDistributionModels;
    }

    @Override
    public Models.DistributionModel updateDistribution(DistributionUpdateForm form) throws NotFoundException, ParseException {


        Optional<Models.Distribution> optionalDistribution = distributionRepo.get(valueOf(form.getId()));
        if (optionalDistribution.isEmpty()) {
            throw new NotFoundException("distribution not found");
        }

        Optional<Models.Purchase> optionalPurchase = purchaseRepo.get(valueOf(optionalDistribution.get().getPurchasesId()));
        if (optionalPurchase.isEmpty()) {
            throw new NotFoundException("purchase with id " + optionalDistribution.get().getPurchasesId() + " not found");
        }

        Models.Distribution updatedDistribution = optionalDistribution.get();

        if (form.getDeleted() != null) {
            updatedDistribution.setDeleted(form.getDeleted());
        }

        if (form.getPaid() != null) {
            updatedDistribution.setPaid(form.getPaid());



            if (form.getPaid()) {
                notificationService.postNotification(new Models.NotificationModels("Payment has been received for your purchase "+updatedDistribution.getBeneficiary(), DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));
                notificationService.postNotification(new Models.NotificationModels("Purchase has been completed successfully", DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getTransporter(), DISTRIBUTION_COLLECTION));

                optionalPurchase.ifPresent(purchase -> purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                    try {
                        notificationService.postNotification(new Models.NotificationModels("Purchase of "+product.getName() + " is successful "+(product.getPrice().multiply(new BigDecimal(items)) + " Ksh"), DistributionStatus.COMPLETE.getDescription(), "Purchase", product.getSellerId(), DISTRIBUTION_COLLECTION));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                })));
            } else {
                notificationService.postNotification(new Models.NotificationModels("You have failed to complete your purchase. This may resolve to deactivation of your account"+updatedDistribution.getBeneficiary(), DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));
                notificationService.postNotification(new Models.NotificationModels(updatedDistribution.getBeneficiary()+" has failed to complete payment and should return products", DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getTransporter(), DISTRIBUTION_COLLECTION));

                optionalPurchase.ifPresent(purchase -> purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                    try {
                        notificationService.postNotification(new Models.NotificationModels("Purchase of "+product.getName() + " has failed and product will be returned ", DistributionStatus.COMPLETE.getDescription(), "Purchase", product.getSellerId(), DISTRIBUTION_COLLECTION));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                })));
            }

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

                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + "  is gathering the items", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));

                    optionalPurchase.ifPresent(purchase -> purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                        if (updatedDistribution.getProductStatus().get(productId) != 2) {
                            try {
                                notificationService.postNotification(new Models.NotificationModels("A transported " + (updatedDistribution.getTransporter()) + " is gathering the items", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Purchase", product.getSellerId(), DISTRIBUTION_COLLECTION));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })));

                    break;

                //ON_THE_WAY
                case 2:

                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + "  is on the way to you" + optionalPurchase.get().getAddress(), DistributionStatus.ON_THE_WAY.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));

                    optionalPurchase.ifPresent(purchase -> purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                        if (updatedDistribution.getProductStatus().get(productId) != 2) {
                            try {
                                notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + "  is on the way to the beneficiary at " + optionalPurchase.get().getAddress(), DistributionStatus.ON_THE_WAY.getDescription(), "Distribution", product.getSellerId(), DISTRIBUTION_COLLECTION));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })));

                    break;

                //ARRIVED
                case 3:
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + "  has arrived at " + optionalPurchase.get().getAddress(), DistributionStatus.ARRIVED.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));
                    notificationService.postNotification(new Models.NotificationModels("You have arrived at " + optionalPurchase.get().getAddress(), DistributionStatus.ARRIVED.getDescription(), "Distribution", updatedDistribution.getTransporter(), DISTRIBUTION_COLLECTION));

                    break;

                //COMPLETE
                case 4:
                    updatedDistribution.setCompletedAt(getNowFormattedFullDate());

                    notificationService.postNotification(new Models.NotificationModels("Purchase has been completed", DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));
                    notificationService.postNotification(new Models.NotificationModels("Purchase has been completed", DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getTransporter(), DISTRIBUTION_COLLECTION));

                    optionalPurchase.ifPresent(purchase -> purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                        if (updatedDistribution.getProductStatus().get(productId) != 2) {
                            try {
                                notificationService.postNotification(new Models.NotificationModels("Purchase has been completed for product " + product.getName(), DistributionStatus.COMPLETE.getDescription(), "Purchase", product.getSellerId(), DISTRIBUTION_COLLECTION));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })));

                    optionalPurchase.ifPresent(p -> {
                        p.setComplete(true);
                        p.setSuccess(true);
                        purchaseRepo.save(p);
                    });
                    break;

                //DNF
                case 5:
                    updatedDistribution.setCompletedAt(getNowFormattedFullDate());
                    Optional<Models.Purchase> optionalPurchase2 = purchaseRepo.get(String.valueOf(updatedDistribution.getPurchasesId()));
                    optionalPurchase2.ifPresent(p -> {
                        p.setComplete(true);
                        p.setSuccess(false);
                        purchaseRepo.save(p);
                    });

                    notificationService.postNotification(new Models.NotificationModels("You have failed to complete your purchase. This may resolve to deactivation of your account"+updatedDistribution.getBeneficiary(), DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getBeneficiary(), DISTRIBUTION_COLLECTION));
                    notificationService.postNotification(new Models.NotificationModels(updatedDistribution.getBeneficiary()+" has failed to complete payment and should return products", DistributionStatus.COMPLETE.getDescription(), "Distribution", updatedDistribution.getTransporter(), DISTRIBUTION_COLLECTION));

                    optionalPurchase.ifPresent(purchase -> purchase.getProducts().forEach((productId, items) -> productService.findProductById(productId).ifPresent(product -> {
                        if (updatedDistribution.getProductStatus().get(productId) != 2) {
                            try {
                                notificationService.postNotification(new Models.NotificationModels("Purchase of " + product.getName() + " has failed and product will be returned ", DistributionStatus.COMPLETE.getDescription(), "Purchase", product.getSellerId(), DISTRIBUTION_COLLECTION));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })));
                    break;
            }
        }

        if (form.getProductStatus() != null) {
            form.getProductStatus().forEach((pid, status) -> {
                Optional<Map.Entry<String, Integer>> pStatus = updatedDistribution.getProductStatus().entrySet().stream().filter(i -> i.getKey().equals(pid)).findFirst();
                pStatus.ifPresent((pStatusObj) -> {
                    pStatusObj.setValue(status);
                    productService.findProductById(pid).ifPresent(product -> {
                        try {
                            switch (status) {
                                //ON_THE_WAY
                                case 1 -> notificationService.postNotification(new Models.NotificationModels("Transporter is on the way to you to collect the product", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Distribution", product.getSellerId(), DISTRIBUTION_COLLECTION));


                                //FAILED
                                case 2 -> notificationService.postNotification(new Models.NotificationModels("Transporter has failed collect your product ( " + product.getName() + ")", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Distribution", product.getSellerId(), DISTRIBUTION_COLLECTION));


                                //COLLECTED
                                case 3 -> notificationService.postNotification(new Models.NotificationModels("Your product has been successfully collected", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Distribution", product.getSellerId(), DISTRIBUTION_COLLECTION));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    });
                });
            });

        }

        updatedDistribution.setUpdatedAt(getNowFormattedFullDate());
        return getDistributionModelFromDistribution(distributionRepo.save(updatedDistribution));
    }

    @Override
    public Models.Remarks createNewRemark(Models.Remarks remarks) throws NotFoundException {

        Models.Remarks savedRemark;

        if (remarks.getDistributionId() != null) {
            Optional<Models.Distribution> optionalDistribution = distributionRepo.get(String.valueOf(remarks.getDistributionId()));
            if (optionalDistribution.isEmpty()) {
                throw new NotFoundException("distribution not found");
            }

            remarks.setId(getNextRemarkId());
            remarks.setDocumentId(String.valueOf(remarks.getId()));
            savedRemark = remarksRepo.save(remarks);

            optionalDistribution.get().setRemarks(savedRemark.getId());
            distributionRepo.save(optionalDistribution.get());

        } else if (remarks.getDonationDistributionId() != null) {
            Optional<Models.DonationDistribution> optionalDonationDistribution = donationDistributionRepo.get(String.valueOf(remarks.getDonationDistributionId()));
            if (optionalDonationDistribution.isEmpty()) {
                throw new NotFoundException("donation distribution not found");
            }

            remarks.setId(getNextRemarkId());
            remarks.setDocumentId(String.valueOf(remarks.getId()));
            savedRemark = remarksRepo.save(remarks);

            optionalDonationDistribution.get().setRemarks(savedRemark.getId());
            donationDistributionRepo.save(optionalDonationDistribution.get());

        } else {
            throw new NotFoundException("distribution not found");
        }

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

    public Optional<Models.Remarks> getRemark(Long id) {
        return remarksRepo.get(String.valueOf(id));
    }

    @Override
    public Models.Donation createNewDonation(DonationCreationForm creationForm) throws NotFoundException, ParseException {

        Long id = getNextDonationId();

        Optional<Models.AppUser> bene = userService.findByUsername(creationForm.getBeneficiary());

        if (bene.isEmpty()) {
            throw new NotFoundException("Beneficiary has not been found");
        }

        Optional<Models.AppUser> donor = userService.findByUsername(creationForm.getDonor());

        if (donor.isEmpty()) {
            throw new NotFoundException("donor has not been found");
        }

        Models.Donation donation = new Models.Donation(id, creationForm.getDonor(), creationForm.getBeneficiary(), getNowFormattedFullDate(), creationForm.getDeliveryLocation(), creationForm.getDeliveryAddress(), creationForm.getCollectionLocation(), creationForm.getCollectionAddress(), false, false, null, null, creationForm.getProducts());
        Models.Donation createdDonation = donationRepo.save(donation);


        if (createdDonation != null) {

            notificationService.postNotification(new Models.NotificationModels(createdDonation.getProducts().size() + " items to be delivered to " + createdDonation.getDeliveryAddress(), createdDonation.getBeneficiaryUsername() + ", you have received a new donation from " + createdDonation.getDonorUsername(), "Donation", createdDonation.getBeneficiaryUsername(), DONATION_COLLECTION));
            notificationService.postNotification(new Models.NotificationModels(createdDonation.getProducts().size() + " items to be delivered to " + createdDonation.getDeliveryAddress(), createdDonation.getDonorUsername() + ", your donation request has been received ", "Donation", createdDonation.getDonorUsername(), DONATION_COLLECTION));

        }

        return createdDonation;
    }

    @Override
    public List<Models.Donation> getDonations(String donorName, String beneficiaryName) {
        return donationRepo.retrieveAll().stream().filter(i -> {
            boolean add = true;

            if (donorName != null) {
                add = i.getDonorUsername().equals(donorName);
            }

            if (beneficiaryName != null) {
                add = i.getBeneficiaryUsername().equals(beneficiaryName);
            }

            return add;
        }).collect(Collectors.toList());
    }

    @Override
    public Models.DonationDistributionModel saveNewDonationDistribution(Long donationId, String transporterUsername) throws NotFoundException, ParseException {

        Optional<Models.Donation> optionalDonation = donationRepo.get(valueOf(donationId));
        if (optionalDonation.isEmpty()) {
            throw new NotFoundException("donation with id " + donationId + " not found");
        }

        Optional<Models.AppUser> optionalTransporter = userService.findByUsername(transporterUsername);
        if (optionalTransporter.isEmpty()) {
            throw new NotFoundException("user with name " + transporterUsername + " not found");
        }


        optionalDonation.get().setAssigned(optionalTransporter.get().getUsername());
        Models.Donation donation = donationRepo.save(optionalDonation.get());


        if (donation != null) {

            Optional<Models.AppUser> beneficiaryOptional = userService.findByUsername(donation.getBeneficiaryUsername());
            if (beneficiaryOptional.isEmpty()) {
                throw new NotFoundException("user with name " + donation.getBeneficiaryUsername() + " not found");
            }

            Models.DonationDistribution distribution = new Models.DonationDistribution(getNextDonationDistributionId(),
                    optionalTransporter.get().getUsername(), beneficiaryOptional.get().getRole().getName().equals(AppRolesEnum.ROLE_BUYER.name()) ? beneficiaryOptional.get().getUsername() : null,
                    optionalDonation.get().getDonorUsername(),
                    DistributionStatus.ACCEPTED.getCode(), getNowFormattedFullDate(), getNowFormattedFullDate(), null, donation.getId(), optionalTransporter.get().getLastKnownLocation(), false, false, null);


            notificationService.postNotification(new Models.NotificationModels("A transporter " + (distribution.getTransporter()) + " has accepted the donation job", "Donation Distribution created", "Donation Distribution", distribution.getDonor(), DONATION_DISTRIBUTION_COLLECTION));
            notificationService.postNotification(new Models.NotificationModels("A transporter " + (distribution.getTransporter()) + " has accepted the donation job", "Donation Distribution created", "Donation Distribution", distribution.getBeneficiary(), DONATION_DISTRIBUTION_COLLECTION));



            return getDonorDistributionModelFromDistributionDonor(donationDistributionRepo.save(distribution));
        } else {
            return null;
        }
    }

    @Override
    public Optional<Models.DonationDistributionModel> getDonorDistributionById(Long id) {
        Optional<Models.DonationDistribution> d = donationDistributionRepo.get(valueOf(id));
        if (d.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getDonorDistributionModelFromDistributionDonor(d.orElse(null)));
    }

    @Override
    public List<Models.DonationDistributionModel> getDonorDistribution(String transporter, String beneficiary, String donorId, Boolean deleted, Long donationId, Integer status, Boolean complete) {
        List<Models.DonationDistribution> allDistributions = donationDistributionRepo.retrieveAll();
        List<Models.DonationDistributionModel> allDistributionModels = new ArrayList<>();

        if (transporter != null) {
            allDistributions.removeIf(i -> !i.getTransporter().equals(transporter));
        }

        if (beneficiary != null) {
            allDistributions.removeIf(i -> !i.getBeneficiary().equals(beneficiary));
        }

        if (donorId != null) {
            allDistributions.removeIf(i -> !i.getDonor().equals(donorId));
        }


        if (deleted != null) {
            allDistributions.removeIf(i -> !(i.getDeleted() == deleted));
        }

        if (complete != null) {
            allDistributions.removeIf(i -> complete == (i.getCompletedAt() != null));
        }

        if (donationId != null) {
            allDistributions.removeIf(i -> !(Objects.equals(i.getDonationId(), donationId)));
        }

        if (status != null) {
            allDistributions.removeIf(i -> !(Objects.equals(i.getStatus(), status)));
        }

        allDistributions.forEach(d -> allDistributionModels.add(getDonorDistributionModelFromDistributionDonor(d)));

        return allDistributionModels;
    }

    @Override
    public Models.DonationDistributionModel updateDonorDistribution(DonorDistributionUpdateForm form) throws NotFoundException, ParseException {

        Optional<Models.DonationDistribution> optionalDistribution = donationDistributionRepo.get(valueOf(form.getId()));
        if (optionalDistribution.isEmpty()) {
            throw new NotFoundException("distribution not found");
        }

        Optional<Models.Donation> optionalDonation = donationRepo.get(String.valueOf(optionalDistribution.get().getDonationId()));
        if (optionalDonation.isEmpty()) {
            throw new NotFoundException("donation not found");
        }

        Models.Donation donation = optionalDonation.get();

        Models.DonationDistribution updatedDistribution = optionalDistribution.get();

        if (form.getDeleted() != null) {
            updatedDistribution.setDeleted(form.getDeleted());
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
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + " is collecting the items", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Donation Distribution", updatedDistribution.getDonor(), DONATION_DISTRIBUTION_COLLECTION));
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + " is collecting the items", DistributionStatus.COLLECTING_ITEMS.getDescription(), "Donation Distribution", updatedDistribution.getBeneficiary(), DONATION_DISTRIBUTION_COLLECTION));

                    break;

                //ON_THE_WAY
                case 2:
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + " is on the way to "+donation.getDeliveryAddress(), DistributionStatus.ON_THE_WAY.getDescription(), "Donation Distribution", updatedDistribution.getDonor(), DONATION_DISTRIBUTION_COLLECTION));
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + " is on the way to you at "+donation.getDeliveryAddress(), DistributionStatus.ON_THE_WAY.getDescription(), "Donation Distribution", updatedDistribution.getBeneficiary(), DONATION_DISTRIBUTION_COLLECTION));

                    break;

                //ARRIVED
                case 3:
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + " has arrived at  "+donation.getDeliveryAddress(), DistributionStatus.ARRIVED.getDescription(), "Donation Distribution", updatedDistribution.getDonor(), DONATION_DISTRIBUTION_COLLECTION));
                    notificationService.postNotification(new Models.NotificationModels("A transporter " + (updatedDistribution.getTransporter()) + " has arrived ", DistributionStatus.ARRIVED.getDescription(), "Donation Distribution", updatedDistribution.getBeneficiary(), DONATION_DISTRIBUTION_COLLECTION));

                    break;

                //COMPLETE
                case 4:
                    updatedDistribution.setCompletedAt(getNowFormattedFullDate());


                    optionalDonation.ifPresent(p -> {
                        p.setComplete(true);
                        p.setSuccess(true);
                        donationRepo.save(p);

                        try {
                            notificationService.postNotification(new Models.NotificationModels("Donation to"+donation.getBeneficiaryUsername() + " is completed successfully", DistributionStatus.COMPLETE.getDescription(), "Donation Distribution", updatedDistribution.getDonor(), DONATION_DISTRIBUTION_COLLECTION));
                            notificationService.postNotification(new Models.NotificationModels("Donation has been complete", DistributionStatus.COMPLETE.getDescription(), "Donation Distribution", updatedDistribution.getBeneficiary(), DONATION_DISTRIBUTION_COLLECTION));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    });
                    break;

                //DNF
                case 5:
                    updatedDistribution.setCompletedAt(getNowFormattedFullDate());
                    Optional<Models.Donation> optionalDonation2 = donationRepo.get(String.valueOf(updatedDistribution.getDonationId()));
                    optionalDonation2.ifPresent(p -> {
                        p.setComplete(true);
                        p.setSuccess(false);
                        donationRepo.save(p);

                        try {
                            notificationService.postNotification(new Models.NotificationModels("Donation to "+donation.getBeneficiaryUsername() + " has failed. Items will be returned", DistributionStatus.DNF.getDescription(), "Donation Distribution", updatedDistribution.getDonor(), DONATION_DISTRIBUTION_COLLECTION));
                            notificationService.postNotification(new Models.NotificationModels("Donation has failed . Items will be returned", DistributionStatus.DNF.getDescription(), "Donation Distribution", updatedDistribution.getBeneficiary(), DONATION_DISTRIBUTION_COLLECTION));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    });
                    break;
            }
        }


        updatedDistribution.setUpdatedAt(getNowFormattedFullDate());

        return getDonorDistributionModelFromDistributionDonor(donationDistributionRepo.save(updatedDistribution));
    }

}
