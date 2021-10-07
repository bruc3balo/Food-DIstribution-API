package com.api.fooddistribution.api.domain;

import com.api.fooddistribution.api.validation.email.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.type.LatLng;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalVariables.*;

public class Models {


    //user

    @Getter
    @Setter
    public static class AppUser {

        @DocumentId
        private String documentId;

        @JsonProperty(value = UID)
        private String uid;

        @JsonProperty(value = NAMES)
        private String names;

        @JsonProperty(USERNAME)
        private String username;

        @JsonProperty(ID_NUMBER)
        private String idNumber;

        @JsonProperty(EMAIL_ADDRESS)
        @ValidEmail(message = "Invalid email")
        private String emailAddress;

        @JsonProperty(PHONE_NUMBER)
        private String phoneNumber;

        @JsonProperty(PASSWORD)
        private String password;

        @JsonProperty(BIO)
        private String bio;

        @JsonProperty(LAST_LOCATION)
        private String lastKnownLocation;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(ROLE)
        private AppRole role;


        @JsonProperty("disabled")
        private boolean disabled;

        @JsonProperty("deleted")
        private boolean deleted;

        public AppUser() {

        }

        public AppUser(String uid, String names, String username, String idNumber, String emailAddress, String phoneNumber, String password, String bio, String lastKnownLocation, Date createdAt, Date updatedAt, AppRole role, boolean disabled, boolean deleted) {
            this.uid = uid;
            this.documentId = uid;
            this.names = names;
            this.username = username;
            this.idNumber = idNumber;
            this.emailAddress = emailAddress;
            this.phoneNumber = phoneNumber;
            this.password = password;
            this.bio = bio;
            this.lastKnownLocation = lastKnownLocation;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.role = role;
            this.disabled = disabled;
            this.deleted = deleted;
        }



    }


    @Getter
    @Setter
    public static class AppRole {
        @DocumentId
        private String documentId;


        @JsonProperty(value = ID)
        private String id;

        @JsonProperty(value = NAME)
        private String name;

        @JsonProperty(PERMISSIONS)
        private List<Permissions> permissions = new ArrayList<>();

        public AppRole() {
        }

        public AppRole(String id, String name) {
            this.id = id;
            this.documentId = id;
            this.name = name;
        }

        public AppRole(String name) {
            this.name = name;
        }

        public AppRole(String id, String name, List<Permissions> allowedPermissions) {
            this.id = id;
            this.documentId = id;
            this.name = name;
            this.permissions = allowedPermissions;
        }

    }

    @Getter
    @Setter
    public static class Permissions {
        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private String id;

        @JsonProperty(NAME)
        private String name;

        public Permissions() {
        }

        public Permissions(String name) {
            this.name = name;
        }

        public Permissions(String id, String name) {
            this.id = id;
            this.documentId = id;
            this.name = name;
        }
    }

    //product

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Product {


        private Long id;


        private String name;

        private ProductCategory productCategory;

        private BigDecimal price;

        private String image;


        private Set<AppUser> sellers = new HashSet<>();


        private Set<AppUser> buyers = new HashSet<>();


        private Date createdAt;


        private Date updatedAt;


        private Boolean deleted;


        private Boolean disabled;

        public Product() {

        }

        public Product(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Product(String name, BigDecimal price, String image, Date createdAt, Date updatedAt, Boolean deleted, Boolean disabled) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deleted = deleted;
            this.disabled = disabled;
        }

        public Product(Long id, String name, Boolean deleted, Boolean disabled) {
            this.id = id;
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
        }
    }

    @Getter
    @Setter

    public static class ProductCategory {

        private Long id;

        private String name;

        private Boolean deleted;

        private Boolean disabled;

        private Date createdAt;

        private Date updatedAt;

        public ProductCategory() {

        }

        public ProductCategory(String name) {
            this.name = name;
        }



        public ProductCategory(String name, Boolean deleted, Boolean disabled, Date createdAt, Date updatedAt) {
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public ProductCategory(Long id, String name, Boolean deleted, Boolean disabled) {
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.id = id;
        }

        public ProductCategory(Long id, String name) {
            this.id =id;
            this.name = name;
        }
    }

    //purchase

    @Getter
    @Setter

    public static class Purchase {

        private Long id;


        private AppUser seller;

        // @Column(name = "buyer")
        private AppUser buyer;


        private Date createdAt;


        private Set<Product> products = new HashSet<>();


        private Boolean deleted;

        public Purchase() {
        }
    }

    @Getter
    @Setter

    public static class Distribution {


        private Long id;

        // @Column(name = "certified_authority")

        public AppUser certifiedAuthority;

        //@Column(name = "transporter")

        public AppUser transporter;


        public Integer status;


        private Date createdAt;


        private Date updatedAt;


        private Date completedAt;


        private Set<Purchase> purchases = new HashSet<>();


        private String lastKnownLocation;


        private Boolean deleted;

        public Distribution() {

        }
    }
}

