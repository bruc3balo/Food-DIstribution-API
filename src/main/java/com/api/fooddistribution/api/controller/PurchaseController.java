package com.api.fooddistribution.api.controller;


import com.api.fooddistribution.api.domain.Models;
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
import java.util.Iterator;
import java.util.List;

import static com.api.fooddistribution.global.GlobalService.purchaseService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.*;

@RestController
@RequestMapping(value = "/purchase")
@Slf4j
public class PurchaseController {

    @PostMapping
    public ResponseEntity<?> addPurchase(@Valid @RequestBody PurchaseCreationForm form) {
        try {

            Models.PurchaseModel savedPurchase = getPurchaseModelFromPurchase(purchaseService.createNewPurchase(form));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), savedPurchase != null ? savedPurchase.getId() + " saved" : "Purhcase not saved", getTransactionId(PURCHASE_COLLECTION), savedPurchase);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save product with name " + form.getBuyerId(), getTransactionId(PURCHASE_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getPurchase(HttpServletRequest request, @RequestParam(name = BUYERS_ID, required = false) String buyerId, @RequestParam(name = SELLERS_ID, required = false) String sellerId) {
        try {

            System.out.println(request.getRequestURL() + " url");
            //System.out.println(request.getServletPath() + " path");
            request.getHeaderNames().asIterator().forEachRemaining(i-> System.out.println(i + ": "+request.getHeader(i)));


            List<String> unknownParams = filterRequestParams(request, Arrays.asList(BUYERS_ID, SELLERS_ID));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<Models.Purchase> purchaseList = purchaseService.getPurchases(buyerId, sellerId);
            List<Models.PurchaseModel> purchaseModelList = new ArrayList<>();
            purchaseList.forEach(purchase -> {
                Models.PurchaseModel purchaseModel = getPurchaseModelFromPurchase(purchase);
                purchaseModelList.add(purchaseModel);
            });



            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), getTransactionId(PURCHASE_COLLECTION), purchaseModelList.stream().sorted());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(PURCHASE_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


}
