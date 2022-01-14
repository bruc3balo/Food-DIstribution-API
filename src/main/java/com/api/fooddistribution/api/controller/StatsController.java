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
import static com.api.fooddistribution.global.GlobalVariables.USERNAME;
import static com.api.fooddistribution.global.GlobalVariables.USER_COLLECTION;
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

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  sellerStats.size() + "found" , getTransactionId(USER_COLLECTION), sellerStats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get users", getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/all_seller"})
    public ResponseEntity<?> getAllSellerStats(HttpServletRequest request, @RequestParam(name = "year") Integer year) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of("year"));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            List<SellerStats> sellerStats = statsService.getAllSellerStats(year);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(),  sellerStats.size() + "found" , getTransactionId(USER_COLLECTION), sellerStats);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get users", getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
