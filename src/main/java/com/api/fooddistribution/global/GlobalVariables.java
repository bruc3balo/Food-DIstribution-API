package com.api.fooddistribution.global;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GlobalVariables {

    public static Algorithm myAlgorithm;

    public static Date accessTokenTime;
    public static Date refreshTokenTime;
    public static String contextPath;

    @Autowired
    public void setMyAlgorithm() {
        //todo encrypt the secret
        GlobalVariables.myAlgorithm = Algorithm.HMAC256("secret".getBytes());
    }
    public static String secretJwt;
    public static String tokenPrefix;
    public static Integer tokenExpirationAfterMin;

    public static String jwtAuthHeader;


    @Autowired
    public void setExpirationDate() {
        GlobalVariables.accessTokenTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    }

    @Autowired
    public void setRefreshTokenTIme() {
        GlobalVariables.refreshTokenTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000 * 10);
    }

    @Value("${server.servlet.context-path}")
    public void setContextPath(String contextPath) {
        GlobalVariables.contextPath = contextPath;
    }


    @Value("${application.jwt.secretKey}")
    public  void setSecretKey(String secretKey) {
        GlobalVariables.secretJwt = secretKey;
    }
    @Value("${application.jwt.tokenPrefix}")
    public void setTokenPrefix(String tokenPrefix) {
        GlobalVariables.tokenPrefix = tokenPrefix;
    }
    @Value("${application.jwt.tokenExpirationAfterMin}")
    public void setTokenExpirationAfterMin(Integer tokenExpirationAfterMin) {
        GlobalVariables.tokenExpirationAfterMin = tokenExpirationAfterMin;
    }

    @Value("Authorization")
    public void setJwtAuthHeader(String jwtAuthHeader) {
        GlobalVariables.jwtAuthHeader = jwtAuthHeader;
    }
}
