package com.api.fooddistribution.global;

import com.api.fooddistribution.api.service.AuthService;
import com.api.fooddistribution.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GlobalService {

    public static UserService userService;
    public static AuthService authService;
    public static UserDetailsService userDetailsService;
    public static PasswordEncoder passwordEncoder;
  //  public static UserAuthenticationServiceImpl userAuthenticationServiceImpl;

/*

    @Autowired
    public void setUserAuthenticationServiceImpl(UserAuthenticationServiceImpl userAuthenticationServiceImpl) {
        GlobalService.userAuthenticationServiceImpl = userAuthenticationServiceImpl;
    }
*/

    @Autowired
    public void setUserService(UserService userService) {
        GlobalService.userService = userService;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        GlobalService.authService = authService;
    }
    

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        GlobalService.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        GlobalService.passwordEncoder = passwordEncoder;
    }

}
