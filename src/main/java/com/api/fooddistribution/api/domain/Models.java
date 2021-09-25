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
        private Date createdAt;

        @Column(name = "updated_at")
        private Date updatedAt;

        @Column(name = "deleted")
        private boolean deleted;

        @ManyToMany(fetch = FetchType.EAGER) //anytime load user, load a role
        @Column(name = "role")
        private Set<AppRole> role = new LinkedHashSet<>();

        public AppUser() {

        }

        public AppUser(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public AppUser(String name) {
            this.name = name;
        }

        public AppUser(String name, String username, String emailAddress, String password) {
            this.name = name;
            this.username = username;
            this.emailAddress = emailAddress;
            this.password = password;
        }

        public AppUser(String name, String username, String emailAddress, String password, boolean deleted, Set<AppRole> roles) {
            this.name = name;
            this.username = username;
            this.emailAddress = emailAddress;
            this.password = password;
            this.role = roles;
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

}

