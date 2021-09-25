package com.api.fooddistribution.config.security;

public enum AppUserPermission {

    //user
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    USER_UPDATE("user:update"),

    //product
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    PRODUCT_DELETE("product:delete"),
    PRODUCT_UPDATE("product:update"),

    //product category
    PRODUCT_CATEGORY_READ("product_category:read"),
    PRODUCT_CATEGORY_WRITE("product_category:write"),
    PRODUCT_CATEGORY_DELETE("product_category:delete"),
    PRODUCT_CATEGORY_UPDATE("product_category:update"),

    //purchase
    PURCHASE_READ("purchase:read"),
    PURCHASE_WRITE("purchase:write"),
    PURCHASE_DELETE("purchase:delete"),
    PURCHASE_UPDATE("purchase:update"),

    //distribution
    DISTRIBUTION_READ("distribution:read"),
    DISTRIBUTION_WRITE("distribution:write"),
    DISTRIBUTION_DELETE("distribution:delete"),
    DISTRIBUTION_UPDATE("distribution:update");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
