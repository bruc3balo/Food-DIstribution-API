package com.api.fooddistribution.api.controller;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.domain.Models.NotificationModels;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.notificationRepo;
import static com.api.fooddistribution.global.GlobalService.notificationService;
import static com.api.fooddistribution.global.GlobalVariables.ID;
import static com.api.fooddistribution.global.GlobalVariables.USERNAME;
import static com.api.fooddistribution.utils.DataOps.filterRequestParams;


@RestController
@RequestMapping("/notification")
public class NotificationController {

    @GetMapping(value = {"/all"})
    public ResponseEntity<?> getNotification(HttpServletRequest request, @RequestParam(value = USERNAME) String username) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of(USERNAME));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<NotificationModels> notifications = notificationService.getUserNotifications(username);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, notifications);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getToShowNotification(HttpServletRequest request, @RequestParam(value = USERNAME) String username) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of(USERNAME));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<NotificationModels> notifications = notificationService.getUnseenUserNotifications(username);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, notifications);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {

            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping
    public ResponseEntity<?> updateNotification(HttpServletRequest request, @RequestParam(value = USERNAME) String username, @RequestParam(value = ID) Long id) {

        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(ID, USERNAME));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Optional<NotificationModels> notification = notificationService.getUserNotifications(username).stream().filter(i-> Objects.equals(i.getId(), id)).findFirst();
            if (notification.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(),"Notification not found", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            notification.get().setNotified(true);
            NotificationModels notificationModels = notificationRepo.save(notification.get());

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, notificationModels);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), e.getLocalizedMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
