package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.model.NewUserForm;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public interface AuthService {

  //  FirebaseSignInSignUpResponseBean authenticateUser(String email, String password);

    boolean isUserPresent(String uid);

    UserRecord authenticateNewUser(NewUserForm newUserForm);

    String sendPasswordChangeRequest (String email) throws FirebaseAuthException;

    boolean deleteUser (String uid);

    UserRecord disableUser (String uid);

    UserRecord enableUser (String uid);

    String sendVerificationEmail(String email);

    void defaults () throws Exception;
    void clearDb () throws Exception;
}
