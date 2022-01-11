package com.api.fooddistribution.api.controller;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DonationCreationForm;
import com.api.fooddistribution.api.model.PurchaseCreationForm;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.api.fooddistribution.global.GlobalService.purchaseService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.*;

@RestController
@RequestMapping(value = "/donation")
@Slf4j
public class DonationController {

    @PostMapping
    public ResponseEntity<?> addDonation(@Valid @RequestBody DonationCreationForm form) {
        try {

            Models.DonationModel savedDonation = getDonationModelFromDonation(purchaseService.createNewDonation(form));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), savedDonation.getId() + " saved", getTransactionId(DONATION_COLLECTION), savedDonation);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save donation for " + form.getDonor(), getTransactionId(DONATION_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getDonation(HttpServletRequest request, @RequestParam(name = BENEFICIARY, required = false) String beneficiary, @RequestParam(name = DONOR, required = false) String donor) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(BENEFICIARY, DONOR));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<Models.Donation> donationList = purchaseService.getDonations(donor, beneficiary);
            List<Models.DonationModel> donationModelList = new ArrayList<>();
            donationList.forEach(donation -> {
                Models.DonationModel donationModel = getDonationModelFromDonation(donation);
                donationModelList.add(donationModel);
            });



            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), getTransactionId(PURCHASE_COLLECTION), donationModelList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(PURCHASE_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


}
