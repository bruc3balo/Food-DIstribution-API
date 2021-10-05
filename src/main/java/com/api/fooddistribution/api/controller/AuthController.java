package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import com.google.firebase.auth.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.api.fooddistribution.global.GlobalService.authService;
import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.getTransactionId;


@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    /*@PostMapping(value = {"authuser"})
    public ResponseEntity<?> authenticateUser(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        try {
            FirebaseSignInSignUpResponseBean signedUpInUser = authService.authenticateUser(email, password);

            if (signedUpInUser != null) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription() + ", " + signedUpInUser.getDisplayName() + " signed in", getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }*/

    @PostMapping(value = {"authnewuser"})
    public ResponseEntity<?> authenticateUser(@RequestBody NewUserForm form) {
        try {
            UserRecord userRecord = authService.authenticateNewUser(form);

            if (userRecord != null) {

                Models.AppUser createdUser = userService.saveAUser(form);

                if (createdUser != null) {
                    JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription() + ", " + userRecord.getDisplayName() + " created", getTransactionId(USER_COLLECTION), createdUser);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription() + ", " + userRecord.getDisplayName() + " created but details not saved", getTransactionId(USER_COLLECTION), userRecord);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"changePassword"})
    public ResponseEntity<?> changePassword(@RequestParam(name = EMAIL_ADDRESS) String email) {
        try {
            String link = authService.sendPasswordChangeRequest(email);
            if (link != null) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription() + ", check email to reset password ", getTransactionId(USER_COLLECTION), link);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"delete_user"})
    public ResponseEntity<?> deleteUser(@RequestParam(name = UID) String uid) {
        try {
            boolean deleted = authService.deleteUser(uid);

            if (deleted) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription() + ", deleted ", getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"disable_user"})
    public ResponseEntity<?> disableUser(@RequestParam(name = UID) String uid) {
        try {
            UserRecord record = authService.disableUser(uid);

            if (record != null) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription() + ", disabled ", getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"enable_user"})
    public ResponseEntity<?> enableUser(@RequestParam(name = UID) String uid) {
        try {
            UserRecord record = authService.enableUser(uid);

            if (record != null) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription() + ", enabled ", getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
