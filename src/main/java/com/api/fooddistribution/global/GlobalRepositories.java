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
    public static PurchaseRepo purchaseRepo;
    public static DistributionRepo distributionRepo;
    public static RemarksRepo remarksRepo;
    public static DonationRepo donationRepo;
    public static DonationDistributionRepo donationDistributionRepo;
    public static NotificationRepo notificationRepo;

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
    public void setCartRepo(CartRepo cartRepo) {
        GlobalRepositories.cartRepo = cartRepo;
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
    public void setPurchaseRepo(PurchaseRepo purchaseRepo) {
        GlobalRepositories.purchaseRepo = purchaseRepo;
    }

    @Autowired
    public void setDistributionRepo(DistributionRepo distributionRepo) {
        GlobalRepositories.distributionRepo = distributionRepo;
    }

    @Autowired
    public void setRemarksRepo(RemarksRepo remarksRepo) {
        GlobalRepositories.remarksRepo = remarksRepo;
    }

    @Autowired
    public void setNotificationRepo(NotificationRepo notificationRepo) {
        GlobalRepositories.notificationRepo = notificationRepo;
    }

    @Autowired
    public void setDonationRepo(DonationRepo donationRepo) {
        GlobalRepositories.donationRepo = donationRepo;
    }

    @Autowired
    public void setDonationDistributionRepo(DonationDistributionRepo donationDistributionRepo) {
        GlobalRepositories.donationDistributionRepo = donationDistributionRepo;
    }
}
