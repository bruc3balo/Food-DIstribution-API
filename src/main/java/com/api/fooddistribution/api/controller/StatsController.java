package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.model.SellerStats;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static com.api.fooddistribution.global.GlobalService.statsService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.*;

@RestController
@RequestMapping(value = "/stats")
@Slf4j
public class StatsController {
    @GetMapping(value = {"/seller"})
    public ResponseEntity<?> getSellerStats(HttpServletRequest request, @RequestParam(name = "year") Integer year, @RequestParam(name = USERNAME) String sellerName) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year",USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> sellerStats = statsService.getSellerStats(year,sellerName);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  sellerStats.size() + "found" , getTransactionId(STATS_COLLECTION), sellerStats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/all_seller"})
    public ResponseEntity<?> getAllSellerStats(HttpServletRequest request, @RequestParam(name = "year") Integer year) {
        try {

            System.out.println("Year is "+year);

            List<String> unknownParams = filterRequestParams(request, List.of("year"));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> sellerStats = statsService.getAllSellerStats(year);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  sellerStats.size() + "found" , getTransactionId(STATS_COLLECTION), sellerStats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/all_donor"})
    public ResponseEntity<?> getAllDonorStats(HttpServletRequest request, @RequestParam(name = "year") Integer year) {
        try {

            System.out.println("Year is "+year);

            List<String> unknownParams = filterRequestParams(request, List.of("year"));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> donorStats = statsService.getAllDonorStats(year);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  donorStats.size() + "found" , getTransactionId(STATS_COLLECTION), donorStats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/donor"})
    public ResponseEntity<?> getDonorStats(HttpServletRequest request, @RequestParam(name = "year") Integer year, @RequestParam(name = USERNAME) String sellerName) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year",USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> donorStats = statsService.getDonorStats(year,sellerName);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  donorStats.size() + "found" , getTransactionId(STATS_COLLECTION), donorStats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"/all_bene_purchase"})
    public ResponseEntity<?> getBeneficiaryReceivedPurchases(HttpServletRequest request, @RequestParam(name = "year") Integer year,@RequestParam(name = USERNAME) String beneficiary) {
        try {

            System.out.println("Year is "+year);

            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year",USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> receivedPurchases = statsService.getBeneficiaryReceivedPurchases(year,beneficiary);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  receivedPurchases.size() + "found" , getTransactionId(STATS_COLLECTION), receivedPurchases);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/all_bene_donations"})
    public ResponseEntity<?> getBeneficiaryReceivedDonations(HttpServletRequest request, @RequestParam(name = "year") Integer year, @RequestParam(name = USERNAME) String beneficiary) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year",USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> receivedDonations = statsService.getBeneficiaryReceivedDonations(year, beneficiary);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  receivedDonations.size() + "found" , getTransactionId(STATS_COLLECTION), receivedDonations);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"/all_trans_donations"})
    public ResponseEntity<?> getTransporterCompletedDonations(HttpServletRequest request, @RequestParam(name = "year") Integer year, @RequestParam(name = USERNAME) String transporter) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year",USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> completedDonations = statsService.getTransporterCompletedDonations(year, transporter);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  completedDonations.size() + "found" , getTransactionId(STATS_COLLECTION), completedDonations);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = {"/all_trans_purchases"})
    public ResponseEntity<?> getTransporterCompletedPurchases(HttpServletRequest request, @RequestParam(name = "year") Integer year, @RequestParam(name = USERNAME) String transporter) {
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList("year",USERNAME));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> completedPurchases = statsService.getTransporterCompletedPurchases(year, transporter);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  completedPurchases.size() + "found" , getTransactionId(STATS_COLLECTION), completedPurchases);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getMessage(), getTransactionId(STATS_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
