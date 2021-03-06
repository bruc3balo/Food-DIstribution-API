package com.api.fooddistribution.config.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.api.fooddistribution.config.security.AppUserPermission.*;


public enum AppRolesEnum {

    ROLE_ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, USER_DELETE, USER_UPDATE, PRODUCT_READ, PRODUCT_WRITE, PRODUCT_DELETE, PRODUCT_UPDATE, PRODUCT_CATEGORY_READ, PRODUCT_CATEGORY_WRITE, PRODUCT_CATEGORY_DELETE, PRODUCT_CATEGORY_UPDATE, PURCHASE_READ, PURCHASE_WRITE, PURCHASE_DELETE, PURCHASE_UPDATE, DISTRIBUTION_DELETE, DISTRIBUTION_UPDATE, DISTRIBUTION_WRITE, DISTRIBUTION_READ)),
    ROLE_ADMIN_TRAINEE(Sets.newHashSet(USER_READ, USER_UPDATE, USER_DELETE, USER_WRITE, PRODUCT_READ, PRODUCT_CATEGORY_READ, PURCHASE_READ, DISTRIBUTION_READ)),
    ROLE_TRANSPORTER(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE    , DISTRIBUTION_READ, DISTRIBUTION_UPDATE)),
    ROLE_CERTIFIED_AUTHORITY(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE, DISTRIBUTION_READ, DISTRIBUTION_UPDATE, DISTRIBUTION_DELETE)),
    ROLE_DISTRIBUTOR(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE)),
    ROLE_BUYER(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE)),
    ROLE_SELLER(Sets.newHashSet(USER_UPDATE, USER_READ, USER_DELETE, USER_WRITE));


    private final Set<AppUserPermission> permissions;

    AppRolesEnum(Set<AppUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }
}
