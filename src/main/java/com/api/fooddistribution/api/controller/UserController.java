package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.filterRequestParams;
import static com.api.fooddistribution.utils.DataOps.getTransactionId;


@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @PostMapping(value = {"/new"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody NewUserForm form) {
        try {

            Models.AppUser savedUser = userService.saveAUser(form);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), savedUser != null ? savedUser.getNames() + " saved" : "User not saved", getTransactionId(USER_COLLECTION), savedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save user with name " + form.getUsername(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/all"})
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getUserList() {
        try {

            List<Models.AppUser> userList = userService.getAllUsers();

          /*  userList.forEach(user-> {
                user.setPassword(HY);
                user.setCreatedAt(HY);
                user.setUpdatedAt(HY);
            });*/

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !userList.isEmpty() ? userList.size() + "users found" : "User Not found", getTransactionId(USER_COLLECTION), userList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get users", getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/numbers")
    //@PreAuthorize("hasIp('')")
    public ResponseEntity<?> getAllPhoneNumbers() {
        try {
            List<String> numbersList = userService.getAllPhoneNumbers();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, numbersList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usernames")
    //@PreAuthorize("hasIp('')")
    public ResponseEntity<?> getAllUsernames() {

        try {
            List<String> usernamesList = userService.getAllUsernames();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, usernamesList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/emails")
    //@PreAuthorize("hasIp('')")
    public ResponseEntity<?> getAllEmails() {

        try {
            List<String> emailList = userService.getAllEmails();
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, emailList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = {"/specific"})
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getUser(@RequestParam(name = UID, required = false) String uid, @RequestParam(name = USERNAME, required = false) String username) {
        try {
            Models.AppUser user;

            if (uid != null) {
                user = userService.getAUserByUid(uid).orElse(null);

            } else if (username != null){
                user = userService.findByUsername(username).orElse(null);

            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "No parameters passed", getTransactionId(USER_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }


             /*       if (user != null) {
                        user.setPassword(HY);
                        user.setCreatedAt(HY);
                        user.setUpdatedAt(HY);
                    }*/


            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), user != null ? user.getNames() + " found" : "User Not found", getTransactionId(USER_COLLECTION), user);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get user with id " + uid, getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = {"/update"})
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestParam(name = UID) String uid, @RequestBody UserUpdateForm updateForm) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of(UID));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            log.info("UPDATED RECEIVED "+new ObjectMapper().writeValueAsString(updateForm) + " for user "+uid);

            Models.AppUser updateUser = userService.updateAUser(uid, updateForm);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), updateUser != null ? updateUser.getNames() + " updated" : "User not updated", getTransactionId(USER_COLLECTION), updateUser);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to update user with id " + uid, getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
