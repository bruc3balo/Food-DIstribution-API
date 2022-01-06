package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DistributionUpdateForm;
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
    List<Models.DistributionModel> getDistribution(String transporter,String beneficiary, String sellerId,String donor,Boolean paid,Boolean deleted,Long purchasesId,Integer status);
    Models.DistributionModel updateDistribution(DistributionUpdateForm form) throws NotFoundException, ParseException;
}
