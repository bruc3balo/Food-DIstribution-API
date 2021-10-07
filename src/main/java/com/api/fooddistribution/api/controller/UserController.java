package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.global.GlobalVariables.HY;
import static com.api.fooddistribution.global.GlobalVariables.USER_COLLECTION;
import static com.api.fooddistribution.utils.DataOps.getTransactionId;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @PostMapping(value = {"/new"})
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
    public ResponseEntity<?> getUserList() {
        try {

            List<Models.AppUser> userList = userService.getAllUsers();

            userList.forEach(user-> {
                user.setPassword(HY);
                user.setCreatedAt(HY);
                user.setUpdatedAt(HY);
            });

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !userList.isEmpty() ? userList.size() + "users found" : "User Not found", getTransactionId(USER_COLLECTION), userList);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get users", getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/specific"})
    public ResponseEntity<?> getUser(@RequestParam(name = "uid", required = false) String uid, @RequestParam(name = "username", required = false) String username) {
        try {
            Models.AppUser user;

            if (uid != null) {
                user = userService.getAUserByUid(uid).orElse(null);

            } else {
                user = userService.findByUsername(username).orElse(null);

            }
            if (user != null) {
                user.setPassword(HY);
                user.setCreatedAt(HY);
                user.setUpdatedAt(HY);
            }


            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), user != null ? user.getNames() + " found" : "User Not found", getTransactionId(USER_COLLECTION), user);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get user with id " + uid, getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = {"/update"})
    public ResponseEntity<?> updateUser(@RequestBody Models.AppUser user) {
        try {

            Models.AppUser updateUser = userService.updateAUser(user);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), updateUser != null ? updateUser.getNames() + " updated" : "User not updated", getTransactionId(USER_COLLECTION), updateUser);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to update user with id " + user.getUid(), getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/delete"})
    public ResponseEntity<?> deleteUser(@RequestParam(name = "uid") String uid) {
        try {

            boolean deleted = userService.deleteUser(uid);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), deleted ? uid + " deleted" : "User Not deleted", getTransactionId(USER_COLLECTION), deleted);
            return new ResponseEntity<>(response, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to delete user with id " + uid, getTransactionId(USER_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
