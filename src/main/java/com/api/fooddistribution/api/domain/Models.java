package com.api.fooddistribution.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Models {


    //user
    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    public static class AppUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        @JsonProperty(value = "name")
        private String name;

        @Column(name = "username", unique = true)
        @JsonProperty(value = "username")
        private String username;

        @Column(name = "email_address")
        @JsonProperty(value = "email_address")
        private String emailAddress;

        @Column(name = "password")
        @JsonProperty(value = "password")
        private String password;

        @Column(updatable = false, name = "created_at")
        @JsonProperty(value = "created_at")
        private Date createdAt;

        @Column(name = "updated_at")
        @JsonProperty(value = "updated_at")
        private Date updatedAt;

        @Column(name = "is_deleted")
        @JsonProperty(value = "is_deleted")
        private Boolean deleted;

        @Column(name = "is_disabled")
        @JsonProperty(value = "is_disabled")
        private Boolean disabled;

        @ManyToOne(fetch = FetchType.EAGER) //anytime load user, load a role
        private AppRole role;

        public AppUser() {

        }

        public AppUser(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public AppUser(String name) {
            this.name = name;
        }

        public AppUser(Long id, String name, String username,String emailAddress) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.emailAddress = emailAddress;
        }

        public AppUser(String name, String username, String emailAddress, String password) {
            this.name = name;
            this.username = username;
            this.emailAddress = emailAddress;
            this.password = password;
        }

        public AppUser(String name, String username, String emailAddress, String password, Boolean deleted, AppRole role) {
            this.name = name;
            this.username = username;
            this.emailAddress = emailAddress;
            this.password = password;
            this.role = role;
            this.deleted = deleted;
        }
    }

    @Entity
    @Table(name = "roles")
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AppRole {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        @JsonProperty(value = "name")
        private String name;

        @ManyToMany(fetch = FetchType.EAGER)
        @Column(name = "permissions")
        private Set<Permissions> allowedPermissions = new LinkedHashSet<>();

        public AppRole() {

        }

        public AppRole(String name) {
            this.name = name;
        }

        public AppRole(String name, Set<Permissions> allowedPermissions) {
            this.name = name;
            this.allowedPermissions = allowedPermissions;
        }

        /*public Set<GrantedAuthority> getGrantedAllowedPermissions() {
            return allowedPermissions.stream().map(permissions -> new SimpleGrantedAuthority(permissions.getName())).collect(Collectors.toSet());
        }*/
    }

    @Entity
    @Table(name = "permissions")
    @Getter
    @Setter
    public static class Permissions {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        public Permissions() {
        }

        public Permissions(String name) {
            this.name = name;
        }


    }

    //product

    @Getter
    @Setter
    @AllArgsConstructor
    @Entity
    @Table(name = "product")
    public static class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "product_name",unique = true)
        private String name;

        @ManyToOne(fetch = FetchType.EAGER)
        private ProductCategory productCategory;

        @Column(name = "price")
        private BigDecimal price;

        @Column(name = "image")
        private String image;

        @Column(name = "seller")
        @ManyToMany(fetch = FetchType.EAGER)
        private Set<AppUser> sellers = new HashSet<>();

        @Column(name = "buyers")
        @ManyToMany(fetch = FetchType.EAGER)
        private Set<AppUser> buyers = new HashSet<>();

        @Column(updatable = false, name = "created_at")
        private Date createdAt;

        @Column(name = "updated_at")
        private Date updatedAt;

        @Column(name = "is_deleted")
        private Boolean deleted;

        @Column(name = "is_disabled")
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
    @AllArgsConstructor
    @Entity
    @Table(name = "product_category")
    public static class ProductCategory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name",unique = true)
        private String name;

        @Column(name = "is_deleted")
        private Boolean deleted;

        @Column(name = "is_disabled")
        private Boolean disabled;

        @Column(updatable = false, name = "created_at")
        private Date createdAt;

        @Column(name = "updated_at")
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
    @AllArgsConstructor
    @Entity
    @Table(name = "purchase")
    public static class Purchase {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // @Column(name = "buyer")
        @OneToOne(fetch = FetchType.EAGER)
        private AppUser buyer;

        @Column(insertable = false, updatable = false, name = "created_at")
        private Date createdAt;

        @Column(name = "product")
        @OneToMany(fetch = FetchType.EAGER)
        private Set<Product> products = new HashSet<>();

        @Column(name = "is_deleted")
        private Boolean deleted;

        public Purchase() {
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Entity
    @Table(name = "distribution")
    public static class Distribution {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // @Column(name = "certified_authority")
        @OneToOne(fetch = FetchType.EAGER)
        public AppUser certifiedAuthority;

        //@Column(name = "transporter")
        @OneToOne(fetch = FetchType.EAGER)
        public AppUser transporter;

        @Column(name = "status")
        public Integer status;

        @Column(name = "created_at")
        private Date createdAt;

        @Column(name = "updated_at")
        private Date updatedAt;

        @Column(name = "completed_at")
        private Date completedAt;

        @Column(name = "purchase")
        @OneToMany(fetch = FetchType.EAGER)
        private Set<Purchase> purchases = new HashSet<>();

        @Column(name = "last_known_location")
        private String lastKnownLocation;

        @Column(name = "is_deleted")
        private Boolean deleted;

        public Distribution() {

        }
    }
}

