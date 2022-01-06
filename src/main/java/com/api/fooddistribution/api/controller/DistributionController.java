package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DistributionUpdateForm;
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
import java.util.Arrays;
import java.util.List;

import static com.api.fooddistribution.global.GlobalService.purchaseService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.*;

@RestController
@RequestMapping(value = "/distribution")
@Slf4j
public class DistributionController {


    @PostMapping
    public ResponseEntity<?> addDistribution(HttpServletRequest request, @RequestParam(name = ID) Long purchaseId, @RequestParam(name = USERNAME) String transporterUsername) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(ID, USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;


            Models.DistributionModel distributionModel = purchaseService.saveNewDistribution(purchaseId,transporterUsername);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), distributionModel != null ? distributionModel.getId() + " saved" : "Distribution not saved", getTransactionId(DISTRIBTUION_COLLECTION), distributionModel);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save distribution from with name " + purchaseId, getTransactionId(DISTRIBTUION_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<?> getDistribution(HttpServletRequest request,
                                             @RequestParam(name = STATUS, required = false) Integer status,
                                             @RequestParam(name = PURCHASE_ID, required = false) Long purchasesId,
                                             @RequestParam(name = BENEFICIARY, required = false) String beneficiary,
                                             @RequestParam(name = TRANSPORTER, required = false) String transporter,
                                             @RequestParam(name = DONOR, required = false) String donor,
                                             @RequestParam(name = PAID, required = false) Boolean paid,
                                             @RequestParam(name = DELETED, required = false) Boolean deleted,
                                             @RequestParam(name = SELLERS_ID, required = false) String sellerId) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(STATUS,TRANSPORTER,PURCHASE_ID,BENEFICIARY,DONOR,PAID,DELETED, SELLERS_ID));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<Models.DistributionModel> distributionModels = purchaseService.getDistribution(transporter,beneficiary,sellerId,donor,paid,deleted,purchasesId,status);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), getTransactionId(DISTRIBTUION_COLLECTION), distributionModels);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(DISTRIBTUION_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping
    public ResponseEntity<?> updateDistribution(@Valid @RequestBody DistributionUpdateForm form) {
        try {

            Models.DistributionModel distributionModels = purchaseService.updateDistribution(form);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), getTransactionId(DISTRIBTUION_COLLECTION), distributionModels);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(DISTRIBTUION_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



}
