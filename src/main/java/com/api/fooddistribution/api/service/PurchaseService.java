package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DistributionUpdateForm;
import com.api.fooddistribution.api.model.DonationCreationForm;
import com.api.fooddistribution.api.model.DonorDistributionUpdateForm;
import com.api.fooddistribution.api.model.PurchaseCreationForm;
import javassist.NotFoundException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface PurchaseService {
    Models.Purchase createNewPurchase(PurchaseCreationForm form) throws NotFoundException, ParseException;
    List<Models.Purchase> getPurchases(String buyerId,String sellerId);
    Models.DistributionModel saveNewDistribution(Long purchaseId,String transporterUsername) throws NotFoundException, ParseException;
    Optional<Models.DistributionModel> getDistributionById(Long id) throws NotFoundException;
    List<Models.DistributionModel> getDistribution(String transporter,String beneficiary, String sellerId,Boolean paid,Boolean deleted,Long purchasesId,Integer status,Boolean complete);
    Models.DistributionModel updateDistribution(DistributionUpdateForm form) throws NotFoundException, ParseException;

    Models.Remarks createNewRemark(Models.Remarks remarks) throws NotFoundException;
    Models.Remarks updateRemark(Models.Remarks remarks) throws NotFoundException;
    Optional<Models.Remarks> getRemark(Long id);

    Models.Donation createNewDonation(DonationCreationForm creationForm) throws NotFoundException, ParseException;
    List<Models.Donation> getDonations(String donorName, String beneficiaryName);
    Models.DonationDistributionModel saveNewDonation(Long donationId,String transporterUsername) throws NotFoundException,ParseException;
    Optional<Models.DonationDistributionModel> getDonorDistributionById(Long id) throws NotFoundException;
    List<Models.DonationDistributionModel> getDonorDistribution(String transporter,String beneficiary, String donorId,Boolean deleted,Long donationId,Integer status,Boolean complete);
    Models.DonationDistributionModel updateDonorDistribution(DonorDistributionUpdateForm updateForm) throws NotFoundException, ParseException;
}
