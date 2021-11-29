package com.api.fooddistribution.api.domain;

import com.api.fooddistribution.api.validation.email.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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
        private String createdAt;

        @JsonProperty(UPDATED_AT)
        private String updatedAt;

        @JsonProperty(ROLE)
        private AppRole role;

        @JsonProperty(DISABLED)
        private boolean disabled;

        @JsonProperty(DELETED)
        private boolean deleted;

        @JsonProperty(TUTORIAL)
        private boolean tutorial;

        @JsonProperty(VERIFIED)
        private boolean verified;

        @JsonProperty(PROFILE_PICTURE)
        private String profilePicture;

        public AppUser() {

        }

        public AppUser(String uid, String names, String username, String idNumber, String emailAddress, String phoneNumber, String password, String bio, String lastKnownLocation, String createdAt, String updatedAt, AppRole role, boolean disabled, boolean deleted,boolean tutorial,boolean verified,String profilePicture) {
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
            this.tutorial = tutorial;
            this.verified = verified;
            this.profilePicture = profilePicture;
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
        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private String id;

        @JsonProperty(NAME)
        private String name;

        @JsonProperty(PRODUCT_CATEGORY_ID)
        private String product_category_id;

        @JsonProperty(PRICE)
        private BigDecimal price;

        @JsonProperty(IMAGE)
        private String image;

        @JsonProperty(SELLERS_ID)
        private String sellerId;

        @JsonProperty(UNITS_LEFT)
        private Double unitsLeft;

        @JsonProperty(CREATED_AT)
        private String createdAt;

        @JsonProperty(UPDATED_AT)
        private String updatedAt;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(DISABLED)
        private Boolean disabled;

        @JsonProperty(UNIT)
        private String unit;

        @JsonProperty(PRODUCT_DESCRIPTION)
        private String product_description;

        public Product() {

        }

        public Product(String id, String name) {
            this.id = id;
            this.name = name;
            this.documentId = id;

        }

        public Product(String name, BigDecimal price, String image, String createdAt, String updatedAt, Boolean deleted, Boolean disabled) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deleted = deleted;
            this.disabled = disabled;
        }

        public Product(String id, String name, String product_category_id, BigDecimal price, String image, String createdAt, String updatedAt, Boolean deleted, Boolean disabled, String unit, String product_description,Double unitsLeft,String sellerId) {
            this.id = id;
            this.name = name;
            this.product_category_id = product_category_id;
            this.price = price;
            this.image = image;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deleted = deleted;
            this.disabled = disabled;
            this.unit = unit;
            this.product_description = product_description;
            this.documentId = id;
            this.sellerId = sellerId;
            this.unitsLeft = unitsLeft;

        }

        public Product(String id, String name, Boolean deleted, Boolean disabled) {
            this.id = id;
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.documentId = id;

        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ProductModel {
        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private String id;

        @JsonProperty(NAME)
        private String name;

        @JsonProperty(PRODUCT_CATEGORY)
        private ProductCategory product_category;

        @JsonProperty(PRICE)
        private BigDecimal price;

        @JsonProperty(IMAGE)
        private String image;

        @JsonProperty(SELLERS_ID)
        private String sellerId;

        @JsonProperty(UNITS_LEFT)
        private Double unitsLeft;

        @JsonProperty(CREATED_AT)
        private String createdAt;

        @JsonProperty(UPDATED_AT)
        private String updatedAt;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(DISABLED)
        private Boolean disabled;

        @JsonProperty(UNIT)
        private String unit;

        @JsonProperty(PRODUCT_DESCRIPTION)
        private String product_description;

        public ProductModel() {

        }

        public ProductModel(String id, String name) {
            this.id = id;
            this.name = name;
            this.documentId = id;

        }

        public ProductModel(String name, BigDecimal price, String image, String createdAt, String updatedAt, Boolean deleted, Boolean disabled) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deleted = deleted;
            this.disabled = disabled;
        }

        public ProductModel(String id, String name, ProductCategory product_category, BigDecimal price, String image, String createdAt, String updatedAt, Boolean deleted, Boolean disabled, String unit, String product_description,Double unitsLeft,String sellerId) {
            this.id = id;
            this.name = name;
            this.product_category = product_category;
            this.price = price;
            this.image = image;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deleted = deleted;
            this.disabled = disabled;
            this.unit = unit;
            this.product_description = product_description;
            this.documentId = id;
            this.sellerId = sellerId;
            this.unitsLeft = unitsLeft;

        }

        public ProductModel(String id, String name, ProductCategory product_category, BigDecimal price, String image, String sellerId, Double unitsLeft, String createdAt, String updatedAt, Boolean deleted, Boolean disabled, String unit, String product_description) {
            this.id = id;
            this.documentId = id;
            this.name = name;
            this.product_category = product_category;
            this.price = price;
            this.image = image;
            this.sellerId = sellerId;
            this.unitsLeft = unitsLeft;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deleted = deleted;
            this.disabled = disabled;
            this.unit = unit;
            this.product_description = product_description;
        }

        public ProductModel(String id, String name, Boolean deleted, Boolean disabled) {
            this.id = id;
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.documentId = id;
        }
    }

    @Getter
    @Setter
    public static class Cart {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private String id;

        @JsonProperty("userId")
        @NotBlank(message = "user id cannot be blank")
        private String userId;

        @JsonProperty("productId")
        @NotBlank(message = "product cannot be blank")
        private String productId;

        @JsonProperty("numberOfItems")
        @NotNull(message = "product cannot be blank")
        private Double numberOfItems;

        public Cart() {
        }

        public Cart(String id, String userId, String productId, Double numberOfItems) {
            this.id = id;
            this.documentId = id;
            this.userId = userId;
            this.productId = productId;
            this.numberOfItems = numberOfItems;
        }

        public Cart(String userId, String productId, Double numberOfItems) {
            this.userId = userId;
            this.productId = productId;
            this.numberOfItems = numberOfItems;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @NonNull
        public String getUserId() {
            return userId;
        }

        public void setUserId(@NonNull String userId) {
            this.userId = userId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Double getNumberOfItems() {
            return numberOfItems;
        }

        public void setNumberOfItems(Double numberOfItems) {
            this.numberOfItems = numberOfItems;
        }
    }

    @Getter
    @Setter
    public static class ProductCategory {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private String id;

        @JsonProperty(NAME)
        private String name;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(DISABLED)
        private Boolean disabled;

        @JsonProperty(CREATED_AT)
        private String createdAt;

        @JsonProperty(UPDATED_AT)
        private String updatedAt;

        public ProductCategory() {

        }

        public ProductCategory(String name) {
            this.name = name;
        }



        public ProductCategory(String name, Boolean deleted, Boolean disabled, String createdAt, String updatedAt) {
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public ProductCategory(String id, String name, Boolean deleted, Boolean disabled) {
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.id = id;
            this.documentId = id;

        }

        public ProductCategory(String id, String name) {
            this.id =id;
            this.documentId = id;
            this.name = name;
        }

        public ProductCategory(String id, String name, Boolean deleted, Boolean disabled, String createdAt, String updatedAt) {
            this.id = id;
            this.documentId = id;
            this.name = name;
            this.deleted = deleted;
            this.disabled = disabled;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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

