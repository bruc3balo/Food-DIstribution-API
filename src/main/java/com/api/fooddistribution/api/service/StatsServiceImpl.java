package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.SellerStats;
import com.api.fooddistribution.config.security.AppRolesEnum;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.userService;

@Service
public class StatsServiceImpl implements StatsService {

    @Override
    public List<SellerStats> getSellerStats(Integer year, String sellerName) {
        List<SellerStats> sellerStatsList = new ArrayList<>();
        List<Models.Purchase> allPurchases = purchaseRepo.retrieveAll();
        List<Models.Product> allProducts = productRepo.retrieveAll().stream().filter(product -> product.getSellerId().equals(sellerName)).collect(Collectors.toList());

        //add all months
        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyProducts = new LinkedHashMap<>();
            allProducts.forEach(product -> emptyProducts.put(product.getId(), 0));
            sellerStatsList.add(new SellerStats(i, emptyProducts));
        }

        allPurchases.stream().filter(Models.Purchase::isComplete).forEach(purchase -> {
            Date purchaseDate = purchase.getCreatedAt();
            Calendar purchaseCalendar = Calendar.getInstance();
            purchaseCalendar.setTime(purchaseDate);


            sellerStatsList.stream().filter(i -> (i.getMonth().equals(purchaseCalendar.get(Calendar.MONTH))) && (purchaseCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> {
                purchase.getProducts().forEach(sellerStats::addProductToMonth);
            });
        });


        return sellerStatsList;
    }

    @Override
    public List<SellerStats> getAllSellerStats(Integer year) {
        List<SellerStats> sellerStatsList = new ArrayList<>();
        List<Models.Purchase> allPurchases = purchaseRepo.retrieveAll();
        List<Models.Product> allProducts = productRepo.retrieveAll();

        //add all months
        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyProducts = new LinkedHashMap<>();
            allProducts.forEach(product -> emptyProducts.put(product.getId(), 0));
            sellerStatsList.add(new SellerStats(i, emptyProducts));
        }

        allPurchases.stream().filter(Models.Purchase::isComplete).forEach(purchase -> {
            Date purchaseDate = purchase.getCreatedAt();
            Calendar purchaseCalendar = Calendar.getInstance();
            purchaseCalendar.setTime(purchaseDate);


            sellerStatsList.stream().filter(i -> (i.getMonth().equals(purchaseCalendar.get(Calendar.MONTH))) && (purchaseCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> {
                purchase.getProducts().forEach(sellerStats::addProductToMonth);
            });
        });

        return sellerStatsList;
    }

    @Override
    public List<SellerStats> getDonorStats(Integer year, String donorName) throws NotFoundException {
        List<SellerStats> donorStats = new ArrayList<>();


        final Optional<Models.AppUser> op = userService.findByUsername(donorName);

        if (donorName == null || op.isEmpty()) {
            throw new NotFoundException("donor name not found");
        }

        if (!op.get().getRole().getName().equals(AppRolesEnum.ROLE_DONOR.name())) {
            throw new IllegalStateException("User not a donor");
        }

        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyDonations = new LinkedHashMap<>();
            emptyDonations.put(donorName, 0);
            donorStats.add(new SellerStats(i, emptyDonations));
        }


        donationRepo.retrieveAll().stream().filter(i -> i.getDonorUsername().equals(donorName) && i.getSuccess()).forEach(donation -> {
            Date donationDate = donation.getCreatedAt();
            Calendar donationCalendar = Calendar.getInstance();
            donationCalendar.setTime(donationDate);

            donorStats.stream().filter(i -> (i.getMonth().equals(donationCalendar.get(Calendar.MONTH))) && (donationCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> { //find stat month
                sellerStats.addProductToMonth(donation.getDonorUsername(), donation.getProducts().size());//add number of items to donor
            });
        }); //donors donations



        return donorStats;
    }

    @Override
    public List<SellerStats> getAllDonorStats(Integer year) {
        List<SellerStats> donorStats = new ArrayList<>();
        List<String> allDonors = userRepo.retrieveAll().stream().filter(i -> i.getRole().getName().equals(AppRolesEnum.ROLE_DONOR.name())).map(Models.AppUser::getUsername).collect(Collectors.toList());

        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyDonations = new LinkedHashMap<>();
            allDonors.forEach(donor -> emptyDonations.put(donor, 0));
            donorStats.add(new SellerStats(i, emptyDonations));
        }


        donationRepo.retrieveAll().stream().filter(Models.Donation::getSuccess).forEach(donation -> {
            Date donationDate = donation.getCreatedAt();
            Calendar donationCalendar = Calendar.getInstance();
            donationCalendar.setTime(donationDate);

            donorStats.stream().filter(i -> (i.getMonth().equals(donationCalendar.get(Calendar.MONTH))) && (donationCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> { //find stat month
                sellerStats.addProductToMonth(donation.getDonorUsername(), donation.getProducts().size());// number of items to donor
                System.out.println("Items are "+donation.getProducts().size() + " Month is "+sellerStats.getMonth());
            });
        });

        return donorStats;
    }

    @Override
    public List<SellerStats> getBeneficiaryReceivedPurchases(Integer year, String beneficiaryName) throws NotFoundException {
        List<SellerStats> beneficiaryStats = new ArrayList<>();


        final Optional<Models.AppUser> op = userService.findByUsername(beneficiaryName);

        if (beneficiaryName == null || op.isEmpty()) {
            throw new NotFoundException("beneficiary name not found");
        }

        if (!op.get().getRole().getName().equals(AppRolesEnum.ROLE_BUYER.name())) {
            throw new IllegalStateException("User not a beneficiary");
        }

        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyDonations = new LinkedHashMap<>();
            emptyDonations.put(beneficiaryName, 0);
            beneficiaryStats.add(new SellerStats(i, emptyDonations));
        }


        purchaseRepo.retrieveAll().stream().filter(i -> i.getBuyerId().equals(beneficiaryName) && i.getSuccess() != null && i.getSuccess()).forEach(donation -> {
            Date donationDate = donation.getCreatedAt();
            Calendar donationCalendar = Calendar.getInstance();
            donationCalendar.setTime(donationDate);

            beneficiaryStats.stream().filter(i -> (i.getMonth().equals(donationCalendar.get(Calendar.MONTH))) && (donationCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> { //find stat month
                sellerStats.addProductToMonth(donation.getBuyerId(), 1);//add number of items to donor
            });
        }); //donors donations



        return beneficiaryStats;
    }

    @Override
    public List<SellerStats> getBeneficiaryReceivedDonations(Integer year, String beneficiaryName) throws NotFoundException {
        List<SellerStats> beneficiaryStats = new ArrayList<>();


        final Optional<Models.AppUser> op = userService.findByUsername(beneficiaryName);

        if (beneficiaryName == null || op.isEmpty()) {
            throw new NotFoundException("beneficiary name not found");
        }

        if (!op.get().getRole().getName().equals(AppRolesEnum.ROLE_BUYER.name())) {
            throw new IllegalStateException("User not a beneficiary");
        }

        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyDonations = new LinkedHashMap<>();
            emptyDonations.put(beneficiaryName, 0);
            beneficiaryStats.add(new SellerStats(i, emptyDonations));
        }


        donationRepo.retrieveAll().stream().filter(i -> i.getBeneficiaryUsername().equals(beneficiaryName)&& i.getSuccess() != null && i.getSuccess()).forEach(donation -> {
            Date donationDate = donation.getCreatedAt();
            Calendar donationCalendar = Calendar.getInstance();
            donationCalendar.setTime(donationDate);

            beneficiaryStats.stream().filter(i -> (i.getMonth().equals(donationCalendar.get(Calendar.MONTH))) && (donationCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> { //find stat month
                sellerStats.addProductToMonth(donation.getBeneficiaryUsername(), 1);//add number of items to donor
            });
        }); //donors donations



        return beneficiaryStats;
    }

    @Override
    public List<SellerStats> getTransporterCompletedDonations(Integer year, String transporterName) throws NotFoundException {
        List<SellerStats> beneficiaryStats = new ArrayList<>();


        final Optional<Models.AppUser> op = userService.findByUsername(transporterName);

        if (transporterName == null || op.isEmpty()) {
            throw new NotFoundException("transporter name not found");
        }

        if (!op.get().getRole().getName().equals(AppRolesEnum.ROLE_TRANSPORTER.name())) {
            throw new IllegalStateException("User not a beneficiary");
        }

        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyDonations = new LinkedHashMap<>();
            emptyDonations.put(transporterName, 0);
            beneficiaryStats.add(new SellerStats(i, emptyDonations));
        }


        donationRepo.retrieveAll().stream().filter(i -> i.getAssigned().equals(transporterName)&& i.getSuccess() != null && i.getSuccess()).forEach(donation -> {
            Date donationDate = donation.getCreatedAt();
            Calendar donationCalendar = Calendar.getInstance();
            donationCalendar.setTime(donationDate);

            beneficiaryStats.stream().filter(i -> (i.getMonth().equals(donationCalendar.get(Calendar.MONTH))) && (donationCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> { //find stat month
                sellerStats.addProductToMonth(donation.getAssigned(), 1);//add number of items to donor
            });
        }); //donors donations



        return beneficiaryStats;
    }

    @Override
    public List<SellerStats> getTransporterCompletedPurchases(Integer year, String transporterName) throws NotFoundException {
        List<SellerStats> beneficiaryStats = new ArrayList<>();


        final Optional<Models.AppUser> op = userService.findByUsername(transporterName);

        if (transporterName == null || op.isEmpty()) {
            throw new NotFoundException("transporter name not found");
        }

        if (!op.get().getRole().getName().equals(AppRolesEnum.ROLE_TRANSPORTER.name())) {
            throw new IllegalStateException("User not a beneficiary");
        }

        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyDonations = new LinkedHashMap<>();
            emptyDonations.put(transporterName, 0);
            beneficiaryStats.add(new SellerStats(i, emptyDonations));
        }


        purchaseRepo.retrieveAll().stream().filter(i -> i.getAssigned().equals(transporterName)&& i.getSuccess() != null && i.getSuccess()).forEach(donation -> {
            Date donationDate = donation.getCreatedAt();
            Calendar donationCalendar = Calendar.getInstance();
            donationCalendar.setTime(donationDate);

            beneficiaryStats.stream().filter(i -> (i.getMonth().equals(donationCalendar.get(Calendar.MONTH))) && (donationCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> { //find stat month
                sellerStats.addProductToMonth(donation.getAssigned(), 1);//add number of items to donor
            });
        }); //donors donations



        return beneficiaryStats;
    }
}
