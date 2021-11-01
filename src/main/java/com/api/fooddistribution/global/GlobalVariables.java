package com.api.fooddistribution.global;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GlobalVariables {

    public static final String HY = "-";
    public static final String PACKAGE = "com.api.fooddistribution";

    public static final String USER_COLLECTION = "users";
    public static final String ROLE_COLLECTION = "roles";
    public static final String PERMISSION_COLLECTION = "permissions";
    public static final String PRODUCT_COLLECTION = "product";
    public static final String PRODUCT_CATEGORY_COLLECTION = "product_category";

    //User
    public static final String UID = "uid";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String ROLE = "role";
    public static final String LAST_LOCATION = "last_known_location";
    public static final String BIO = "bio";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String ID_NUMBER = "id_number";
    public static final String NAMES = "names";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";



    public static final String PRODUCT = "Product";
    public static final String PRODUCT_CATEGORY = "ProductCategory";
    public static final String DISABLED = "disabled";
    public static final String DELETED = "deleted";
    public static final String TUTORIAL = "tutorial";
    public static final String VERIFIED = "verified";

    //Role
    public static final String NAME = "name";
    public static final String ID = "id";

    //permissions
    public static final String PERMISSIONS = "permissions";


    //product
    public static final String PRICE = "price";
    public static final String IMAGE = "image";
    public static final String SELLERS = "sellers";
    public static final String SELLERS_ID = "sellersId";
    public static final String BUYERS = "buyers";
    public static final String BUYERS_ID = "buyersId";
    public static final String UNIT = "unit";
    public static final String PRODUCT_CATEGORY_NAME = "product_category_name";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_DESCRIPTION = "product_description";

    public static Algorithm myAlgorithm;

    public static Date accessTokenTime;
    public static Date refreshTokenTime;
    public static String contextPath;


    public static String secretJwt;
    public static String tokenPrefix;
    public static Integer tokenExpirationAfterMin;

    public static String jwtAuthHeader;

    @Autowired
    public void setMyAlgorithm() {
        //todo encrypt the secret
        GlobalVariables.myAlgorithm = Algorithm.HMAC256("secret".getBytes());
    }

    @Autowired
    public void setExpirationDate() {
        GlobalVariables.accessTokenTime = new Date(System.currentTimeMillis() + 86400000);
    }

    @Autowired
    public void setRefreshTokenTIme() {
        GlobalVariables.refreshTokenTime = new Date(System.currentTimeMillis() + 172800000);
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
