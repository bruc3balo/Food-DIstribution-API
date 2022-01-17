package com.api.fooddistribution.api.domain;

import com.api.fooddistribution.api.model.DonorItem;
import com.api.fooddistribution.api.model.ProductCountModel;
import com.api.fooddistribution.api.validation.email.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedFullDate;

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

        public AppUser(String username) {
            this.username = username;
        }

        public AppUser(String uid, String names, String username, String idNumber, String emailAddress, String phoneNumber, String password, String bio, String lastKnownLocation, String createdAt, String updatedAt, AppRole role, boolean disabled, boolean deleted, boolean tutorial, boolean verified, String profilePicture) {
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

        @JsonProperty(LOCATION)
        private String location;

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

        public Product(String id, String name, String product_category_id, BigDecimal price, String image, String createdAt, String updatedAt, Boolean deleted, Boolean disabled, String unit, String product_description, Double unitsLeft, String sellerId, String location) {
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
            this.location = location;

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

        @JsonProperty(LOCATION)
        private String location;

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

        public ProductModel(String id, String name, ProductCategory product_category, BigDecimal price, String image, String createdAt, String updatedAt, Boolean deleted, Boolean disabled, String unit, String product_description, Double unitsLeft, String sellerId) {
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

        public ProductModel(String id, String name, ProductCategory product_category, BigDecimal price, String image, String sellerId, Double unitsLeft, String createdAt, String updatedAt, Boolean deleted, Boolean disabled, String unit, String product_description, String location) {
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
            this.location = location;
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
            this.id = id;
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

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty(BUYERS_ID)
        private String buyerId;

        @JsonProperty(LOCATION)
        private String location;

        @JsonProperty(ADDRESS)
        private String address;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(PRODUCT)
        private LinkedHashMap<String, Integer> products = new LinkedHashMap<>();

        private boolean deleted;

        private boolean complete;

        private Boolean success;

        private String assigned;


        public Purchase() {

        }

        public Purchase(Long id) {
            this.id = id;
        }

        public Purchase(Long id, String location, String address, String buyerId, Date createdAt, LinkedHashMap<String, Integer> products,Boolean success) {
            this.id = id;
            this.documentId = String.valueOf(id);
            this.buyerId = buyerId;
            this.location = location;
            this.createdAt = createdAt;
            this.products = products;
            this.address = address;
            this.success = success;
        }
    }

    @Getter
    @Setter
    public static class PurchaseModel {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty(BUYERS_ID)
        private String buyerId;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(LOCATION)
        private String location;

        @JsonProperty(ADDRESS)
        private String address;

        @JsonProperty(PRODUCT)
        private LinkedHashSet<ProductCountModel> products = new LinkedHashSet<>();

        private boolean deleted;

        private boolean complete;

        private Boolean success;

        private String assigned;

        public PurchaseModel() {

        }

        public PurchaseModel(Long id, String buyerId, String location, String address, Date createdAt, LinkedHashSet<ProductCountModel> products, boolean deleted, boolean complete,Boolean success,String assigned) {
            this.id = id;
            this.documentId = String.valueOf(id);
            this.buyerId = buyerId;
            this.createdAt = createdAt;
            this.products = products;
            this.location = location;
            this.address = address;
            this.deleted = deleted;
            this.complete = complete;
            this.assigned = assigned;
            this.success = success;
        }
    }

    @Getter
    @Setter
    public static class Distribution {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty(TRANSPORTER)
        private String transporter;

        @JsonProperty(BENEFICIARY)
        private String beneficiary;

        @JsonProperty(STATUS)
        private Integer status;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(COMPLETED_AT)
        private Date completedAt;

        @JsonProperty(PURCHASE_ID)
        private Long purchasesId;

        @JsonProperty(LAST_LOCATION)
        private String lastKnownLocation;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(PAID)
        private Boolean paid;

        @JsonProperty(REPORTED)
        private Boolean reported;

        @JsonProperty(REMARKS)
        private Long remarks;

        @JsonProperty(PRODUCT_STATUS)
        private Map<String, Integer> productStatus;


        public Distribution() {

        }

        public Distribution(Long id, String transporter, String beneficiary, Integer status, Date createdAt, Date updatedAt, Date completedAt, Long purchasesId, String lastKnownLocation, Boolean deleted, Boolean paid, Boolean reported, Long remarks, Map<String, Integer> productStatus) {
            this.id = id;
            this.documentId = String.valueOf(id);

            this.transporter = transporter;
            this.beneficiary = beneficiary;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.completedAt = completedAt;
            this.purchasesId = purchasesId;
            this.lastKnownLocation = lastKnownLocation;
            this.deleted = deleted;
            this.paid = paid;
            this.reported = reported;
            this.remarks = remarks;
            this.productStatus = productStatus;
        }
    }

    @Getter
    @Setter
    public static class DistributionModel {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty(TRANSPORTER)
        public AppUser transporter;

        @JsonProperty(BENEFICIARY)
        public AppUser beneficiary;

        @JsonProperty(STATUS)
        public Integer status;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(COMPLETED_AT)
        private Date completedAt;

        @JsonProperty(PURCHASE_COLLECTION)
        private Models.PurchaseModel purchases;

        @JsonProperty(LAST_LOCATION)
        private String lastKnownLocation;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(PAID)
        private Boolean paid;

        @JsonProperty(REPORTED)
        private Boolean reported;

        @JsonProperty(REMARKS)
        private Remarks remarks;

        @JsonProperty(PRODUCT_STATUS)
        private Map<String, Integer> productStatus;


        public DistributionModel() {

        }

        public DistributionModel(String documentId, Long id, AppUser transporter, AppUser beneficiary, Integer status, Date createdAt, Date updatedAt, Date completedAt, PurchaseModel purchases, String lastKnownLocation, Boolean deleted, Boolean paid, Boolean reported, Remarks remarks,Map<String, Integer> productStatus) {
            this.documentId = documentId;
            this.id = id;
            this.transporter = transporter;
            this.beneficiary = beneficiary;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.completedAt = completedAt;
            this.purchases = purchases;
            this.lastKnownLocation = lastKnownLocation;
            this.deleted = deleted;
            this.paid = paid;
            this.reported = reported;
            this.remarks = remarks;
            this.productStatus = productStatus;
        }
    }

    @Getter
    @Setter
    public static class Remarks {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty(DONOR)
        private String donor;

        @JsonProperty(TRANSPORTER)
        private String transporter;

        @JsonProperty(BENEFICIARY)
        private String beneficiary;

        @JsonProperty(DISTRIBUTION_ID)
        private Long distributionId;

        @JsonProperty(DONATION_DISTRIBUTION_ID)
        private Long donationDistributionId;


        public Remarks() {

        }

        public Remarks(Long id) {
            this.id = id;
        }


    }

    @Getter
    @Setter
    public static class Donation {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty("donor_username")
        private String donorUsername;

        @JsonProperty("beneficiary_username")
        private String beneficiaryUsername;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(DELIVERY_LOCATION)
        private String deliveryLocation;

        @JsonProperty(DELIVERY_ADDRESS)
        private String deliveryAddress;

        @JsonProperty(COLLECTION_LOCATION)
        private String collectionLocation;

        @JsonProperty(COLLECTION_ADDRESS)
        private String collectionAddress;

        private boolean deleted;

        private boolean complete;

        private Boolean success;

        private String assigned;

        private LinkedList<DonorItem> products = new LinkedList<>();


        public Donation() {

        }

        public Donation(Long id) {
            this.id = id;
        }


        public Donation(Long id, String donorUsername, String beneficiaryUsername, Date createdAt, String deliveryLocation, String deliveryAddress, String collectionLocation, String collectionAddress, boolean deleted, boolean complete, Boolean success,String assigned, LinkedList<DonorItem> products) {
            this.id = id;
            this.documentId = String.valueOf(id);
            this.donorUsername = donorUsername;
            this.beneficiaryUsername = beneficiaryUsername;
            this.createdAt = createdAt;
            this.deliveryLocation = deliveryLocation;
            this.deliveryAddress = deliveryAddress;
            this.collectionLocation = collectionLocation;
            this.collectionAddress = collectionAddress;
            this.deleted = deleted;
            this.complete = complete;
            this.assigned = assigned;
            this.products = products;
            this.success = success;
        }
    }

    @Getter
    @Setter
    public static class DonationModel {

        @JsonProperty(ID)
        private Long id;

        @JsonProperty("donor_username")
        private AppUser donorUsername;

        @JsonProperty("beneficiary_username")
        private AppUser beneficiaryUsername;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(DELIVERY_LOCATION)
        private String deliveryLocation;

        @JsonProperty(DELIVERY_ADDRESS)
        private String deliveryAddress;

        @JsonProperty(COLLECTION_LOCATION)
        private String collectionLocation;

        @JsonProperty(COLLECTION_ADDRESS)
        private String collectionAddress;

        private boolean deleted;

        private boolean complete;

        private Boolean success;

        private String assigned;

        private LinkedList<DonorItem> products = new LinkedList<>();


        public DonationModel() {

        }

        public DonationModel(Long id) {
            this.id = id;
        }

        public DonationModel(Long id, AppUser donorUsername, AppUser beneficiaryUsername, Date createdAt, String deliveryLocation, String deliveryAddress, String collectionLocation, String collectionAddress, boolean deleted, boolean complete, Boolean success,String assigned, LinkedList<DonorItem> products) {
            this.id = id;
            this.donorUsername = donorUsername;
            this.beneficiaryUsername = beneficiaryUsername;
            this.createdAt = createdAt;
            this.deliveryLocation = deliveryLocation;
            this.deliveryAddress = deliveryAddress;
            this.collectionLocation = collectionLocation;
            this.collectionAddress = collectionAddress;
            this.deleted = deleted;
            this.complete = complete;
            this.assigned = assigned;
            this.products = products;
            this.success = success;
        }
    }


    @Getter
    @Setter
    public static class DonationDistribution {

        @DocumentId
        private String documentId;

        @JsonProperty(ID)
        private Long id;

        @JsonProperty(TRANSPORTER)
        private String transporter;

        @JsonProperty(BENEFICIARY)
        private String beneficiary;

        @JsonProperty(DONOR)
        private String donor;

        @JsonProperty(STATUS)
        private Integer status;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(COMPLETED_AT)
        private Date completedAt;

        @JsonProperty(DONATION_ID)
        private Long donationId;

        @JsonProperty(LAST_LOCATION)
        private String lastKnownLocation;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(REPORTED)
        private Boolean reported;

        @JsonProperty(REMARKS)
        private Long remarks;



        public DonationDistribution() {

        }

        public DonationDistribution(Long id, String transporter, String beneficiary, String donor, Integer status, Date createdAt, Date updatedAt, Date completedAt, Long donationId, String lastKnownLocation, Boolean deleted, Boolean reported, Long remarks) {
            this.documentId = String.valueOf(id);
            this.id = id;
            this.transporter = transporter;
            this.beneficiary = beneficiary;
            this.donor = donor;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.completedAt = completedAt;
            this.donationId = donationId;
            this.lastKnownLocation = lastKnownLocation;
            this.deleted = deleted;
            this.reported = reported;
            this.remarks = remarks;
        }


    }


    @Getter
    @Setter
    public static class DonationDistributionModel {


        @JsonProperty(ID)
        private Long id;

        @JsonProperty(TRANSPORTER)
        private AppUser transporter;

        @JsonProperty(BENEFICIARY)
        private AppUser beneficiary;

        @JsonProperty(DONOR)
        private AppUser donor;

        @JsonProperty(STATUS)
        private Integer status;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(UPDATED_AT)
        private Date updatedAt;

        @JsonProperty(COMPLETED_AT)
        private Date completedAt;

        @JsonProperty(DONATION_ID)
        private DonationModel donationId;

        @JsonProperty(LAST_LOCATION)
        private String lastKnownLocation;

        @JsonProperty(DELETED)
        private Boolean deleted;

        @JsonProperty(REPORTED)
        private Boolean reported;

        @JsonProperty(REMARKS)
        private Remarks remarks;



        public DonationDistributionModel() {

        }


        public DonationDistributionModel(Long id) {
            this.id = id;
        }

        public DonationDistributionModel(Long id, AppUser transporter, AppUser beneficiary, AppUser donor, Integer status, Date createdAt, Date updatedAt, Date completedAt, DonationModel donationId, String lastKnownLocation, Boolean deleted, Boolean reported, Remarks remarks) {

            this.id = id;
            this.transporter = transporter;
            this.beneficiary = beneficiary;
            this.donor = donor;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.completedAt = completedAt;
            this.donationId = donationId;
            this.lastKnownLocation = lastKnownLocation;
            this.deleted = deleted;
            this.reported = reported;
            this.remarks = remarks;
        }


    }


    @Getter
    @Setter
    public static class NotificationModels {

        //todo notification model
        @DocumentId
        private String documentId;

        private Long id;

        @JsonProperty(DESCRIPTION)
        private String description;

        @JsonProperty(TITLE)
        private String title;

        @JsonProperty(SUBTEXT)
        private String subText;

        @JsonProperty(UID)
        private String uid;

        @JsonProperty(CREATED_AT)
        private Date createdAt;

        @JsonProperty(NOTIFIED)
        private boolean notified;

        @JsonProperty(UPDATING)
        private String updating;

        public NotificationModels () {

        }

        public NotificationModels(String description, String title, String subText, String uid,String updating) throws ParseException {
            this.description = description;
            this.title = title;
            this.subText = subText;
            this.uid = uid;
            this.notified = false;
            this.updating = updating;
            this.createdAt = getNowFormattedFullDate();
        }

    }

}

