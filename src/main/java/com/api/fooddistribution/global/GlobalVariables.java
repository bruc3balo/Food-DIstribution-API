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
    public static final String PURCHASE_COLLECTION = "purchase";
    public static final String DISTRIBUTION_COLLECTION = "distribution";
    public static final String REMARKS_COLLECTION = "remarks";
    public static final String CART_COLLECTION = "cart";
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
    public static final String PROFILE_PICTURE = "profile_picture";

    public static final String PRODUCT = "Product";
    public static final String PRODUCT_CATEGORY = "product_category";
    public static final String DISABLED = "disabled";
    public static final String DELETED = "deleted";
    public static final String TUTORIAL = "tutorial";
    public static final String VERIFIED = "verified";

    //Role
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String DONOR = "donor";
    public static final String REPORTED = "reported";
    public static final String REMARKS = "remarks";
    public static final String TRANSPORTER = "transporter";
    public static final String BENEFICIARY = "beneficiary";
    public static final String DISTRIBUTION_ID = "distribution_id";
    public static final String STATUS = "status";
    public static final String PRODUCT_STATUS = "product_status";
    public static final String COMPLETED_AT = "completed_at";
    public static final String PURCHASE_ID = "purchasesId";
    public static final String PAID = "paid";
    public static final String COMPLETE = "complete";
    public static final String REVIEW = "review";

    //permissions
    public static final String PERMISSIONS = "permissions";


    //product
    public static final String PRICE = "price";
    public static final String IMAGE = "image";
    public static final String SELLERS = "sellers";
    public static final String SELLERS_ID = "sellersId";
    public static final String BUYERS = "buyers";
    public static final String BUYERS_ID = "buyersId";
    public static final String LOCATION = "location";
    public static final String ADDRESS = "address";
    public static final String UNIT = "unit";
    public static final String UNITS_LEFT = "unitsLeft";
    public static final String PRODUCT_CATEGORY_NAME = "product_category_name";
    public static final String PRODUCT_CATEGORY_ID = "product_category_id";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String SOLID = "Solid";
    public static final String LIQUID = "Liquid";
    public static final String GAS = "Gas";
    public static final String PRODUCT_AMOUNT = "productAmount";


    public static final String ACCESS_TOKEN = "access_token";


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
