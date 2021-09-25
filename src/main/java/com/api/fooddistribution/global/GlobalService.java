package com.api.fooddistribution.global;

import com.api.fooddistribution.api.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GlobalService {

    public static DataService dataService;
    public static UserDetailsService userDetailsService;
    public static PasswordEncoder passwordEncoder;



    @Autowired
    public void setDataService(DataService dataService) {
        GlobalService.dataService = dataService;
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
