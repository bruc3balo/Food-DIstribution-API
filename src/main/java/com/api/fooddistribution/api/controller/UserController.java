package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.api.model.RoleToUserForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.api.specification.UserPredicate;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.utils.DataOps.filterRequestParams;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                         @RequestParam(value = "email_address", required = false) String email,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "username", required = false) String username,
                                         @RequestParam(value = "id", required = false) Long id) {


        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "username", "email_address"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }


            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;


            Page<Models.AppUser> userList = userService.getAllUsers(new UserPredicate(new Models.AppUser(id, name, username, email)), PageRequest.of(pageNumber, pageSze));

            userList.forEach(u -> u.getRole().setAllowedPermissions(u.getRole().getAllowedPermissions().stream().sorted(Comparator.comparing(Models.Permissions::getId)).collect(Collectors.toCollection(LinkedHashSet::new))));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, userList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/save")
    //@PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> saveUser(@Valid @RequestBody NewUserForm newUserForm) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        log.info("uri saveuser ::: {}", uri);

        try {
            Models.AppUser savedUser = userService.saveAUser(newUserForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(savedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), savedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, savedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "update/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> updateUser(HttpServletRequest request,
                                        @RequestParam(value = "username") String username,
                                        @RequestBody UserUpdateForm updateForm) {

        try {
            List<String> unknownParams = filterRequestParams(request, List.of("username"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.AppUser updatedUser = userService.updateAUser(username, updateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/saveRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> saveRole(@RequestBody @Valid RoleCreationForm form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/saveRole").toUriString());
        log.info("uri saverole ::: {}", uri);

        try {
            Models.AppRole savedRole = userService.saveANewRole(form);
            JsonResponse response = JsonSetSuccessResponse.setResponse(savedRole != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), savedRole != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, savedRole);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/role2user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/addroletouser").toUriString());


        try {
            userService.addARoleToAUser(form.getUsername(), form.getRoleName());
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> deleteUser(HttpServletRequest request,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "username"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.AppUser> userToBeDeleted = userService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, 1)).getContent();

            if (userToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Models.AppUser updatedUser = userService.deleteUser(userToBeDeleted.get(0));


            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "disable")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> disableUser(HttpServletRequest request,
                                         @RequestParam(value = "disabled") Boolean disabled,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "username", required = false) String username,
                                         @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "username", "disabled"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.AppUser> userToBeDeleted = userService.getAllUsers(new UserPredicate(id, name, username), PageRequest.of(0, 1)).getContent();

            if (userToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (userToBeDeleted.get(0).getDisabled() == disabled) {
                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "User already has that status", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            Models.AppUser updatedUser;
            if (disabled) {
                updatedUser = userService.disableUser(userToBeDeleted.get(0));
            } else {
                updatedUser = userService.enableUser(userToBeDeleted.get(0));
            }

            JsonResponse response = JsonSetSuccessResponse.setResponse(updatedUser != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updatedUser != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

