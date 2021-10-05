package com.api.fooddistribution.global;



import com.api.fooddistribution.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalRepositories {

    public static UserRepo userRepo;
    public static AppRoleRepo appRoleRepo;
    public static PermissionsRepo permissionsRepo;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        GlobalRepositories.userRepo = userRepo;
    }

    @Autowired
    public void setAppRoleRepo(AppRoleRepo appRoleRepo) {
        GlobalRepositories.appRoleRepo = appRoleRepo;
    }

    @Autowired
    public  void setPermissionsRepo(PermissionsRepo permissionsRepo) {
        GlobalRepositories.permissionsRepo = permissionsRepo;
    }
}
