package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.sun.jdi.request.DuplicateRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.api.fooddistribution.config.FirestoreConfig.firebaseAuth;
import static com.api.fooddistribution.global.GlobalService.*;
import static com.google.firebase.auth.UserRecord.UpdateRequest;

@Service
public class AuthServiceImp implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);


  /*  @Override
    public FirebaseSignInSignUpResponseBean authenticateUser(String email, String password) {
        return userAuthenticationServiceImpl.signInWithEmailAndPassword(email, password);
    }*/

    @Override
    public boolean isUserPresent(String username) {
        return userService.findByUsername(username).isPresent();
    }

    @Override
    public UserRecord authenticateNewUser(NewUserForm form) {


        try {
            if (!isUserPresent(form.getUsername())) {
                CreateRequest createRequest = new CreateRequest();

                createRequest.setDisabled(false);
                createRequest.setDisplayName(form.getUsername());
                createRequest.setEmail(form.getEmailAddress());
                createRequest.setPhoneNumber(form.getPhoneNumber());
                createRequest.setPassword(passwordEncoder.encode(form.getPassword()));
                createRequest.setEmailVerified(false);

                UserRecord record = firebaseAuth.createUser(createRequest);
                form.setUid(record.getUid());
                return record;
            } else {
                throw new DuplicateRequestException("user with name,"+form.getUsername() +" already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" AUTH ERROR ::: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String sendPasswordChangeRequest(String email) throws FirebaseAuthException {
        return firebaseAuth.generatePasswordResetLink(email);
    }

    @Override
    public boolean deleteUser(String uid) {
        try {
            Models.AppUser user = userService.getAUserByUid(uid).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            firebaseAuth.deleteUser(user.getUid());
            return userService.deleteUser(user.getUid());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserRecord disableUser(String uid) {
        try {

            Models.AppUser user = userService.getAUserByUid(uid).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            userService.disableUser(user);

            UpdateRequest updateRequest = new UpdateRequest(user.getUid());
            updateRequest.setDisabled(true);
            return firebaseAuth.updateUser(updateRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public UserRecord enableUser(String uid) {
        try {

            Models.AppUser user = userService.getAUserByUid(uid).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            userService.disableUser(user);

            UpdateRequest updateRequest = new UpdateRequest(user.getUid());
            updateRequest.setDisabled(true);

            return firebaseAuth.updateUser(updateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
