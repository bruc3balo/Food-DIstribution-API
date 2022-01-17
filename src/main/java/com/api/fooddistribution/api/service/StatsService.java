package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.model.SellerStats;
import javassist.NotFoundException;

import java.util.List;

public interface StatsService {
    List<SellerStats> getSellerStats(Integer year, String sellerName);

    List<SellerStats> getAllSellerStats(Integer year);

    List<SellerStats> getDonorStats(Integer year, String donorName) throws NotFoundException;

    List<SellerStats> getAllDonorStats(Integer year);

    List<SellerStats> getBeneficiaryReceivedPurchases(Integer year, String beneficiaryName) throws NotFoundException;

    List<SellerStats> getBeneficiaryReceivedDonations(Integer year, String beneficiaryName) throws NotFoundException;

    List<SellerStats> getTransporterCompletedDonations(Integer year, String transporterName) throws NotFoundException;

    List<SellerStats> getTransporterCompletedPurchases(Integer year, String transporterName) throws NotFoundException;

}
