package com.api.fooddistribution.global;



import com.api.fooddistribution.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalRepositories {

    public static UserRepo userRepo;
    public static CartRepo cartRepo;
    public static AppRoleRepo appRoleRepo;
    public static PermissionsRepo permissionsRepo;
    public static ProductRepo productRepo;
    public static ProductCategoryRepo productCategoryRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        GlobalRepositories.userRepo = userRepo;
    }

    @Autowired
    public void setAppRoleRepo(AppRoleRepo appRoleRepo) {
        GlobalRepositories.appRoleRepo = appRoleRepo;
    }

    @Autowired
    public void setPermissionsRepo(PermissionsRepo permissionsRepo) {
        GlobalRepositories.permissionsRepo = permissionsRepo;
    }

    @Autowired
    public void setProductRepo(ProductRepo productRepo) {
        GlobalRepositories.productRepo = productRepo;
    }

    @Autowired
    public void setProductCategoryRepo(ProductCategoryRepo productCategoryRepo) {
        GlobalRepositories.productCategoryRepo = productCategoryRepo;
    }
}
