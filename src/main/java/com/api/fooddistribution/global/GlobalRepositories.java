package com.api.fooddistribution.global;



import com.api.fooddistribution.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalRepositories {

    public static AppUserRepo userRepo;
    public static AppRoleRepo roleRepo;
    public static AppPermissionRepo appPermissionRepo;
    public static ProductRepo productRepo;
    public static ProductCategoryRepo productCategoryRepo;


    @Autowired
    public void setAppUserRepo(AppUserRepo appUserRepo) {
        GlobalRepositories.userRepo = appUserRepo;
    }

    @Autowired
    public void setAppRoleRepo(AppRoleRepo appRoleRepo) {
        GlobalRepositories.roleRepo = appRoleRepo;
    }

    @Autowired
    public void setProductRepo(ProductRepo productRepo) {
        GlobalRepositories.productRepo = productRepo;
    }

    @Autowired
    public void setProductCategoryRepo(ProductCategoryRepo productCategoryRepo) {
        GlobalRepositories.productCategoryRepo = productCategoryRepo;
    }

    @Autowired
    public void setAppPermissionRepo(AppPermissionRepo appPermissionRepo) {GlobalRepositories.appPermissionRepo = appPermissionRepo;}
}
