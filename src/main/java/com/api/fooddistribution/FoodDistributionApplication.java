package com.api.fooddistribution;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.api.specification.UserPredicate;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.config.security.AppUserPermission;
import com.api.fooddistribution.utils.ConvertToJson;
import com.api.fooddistribution.utils.DataOps;
import com.google.common.collect.Sets;
import javassist.NotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.dataService;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class FoodDistributionApplication {


    public static void main(String[] args) {
        SpringApplication.run(FoodDistributionApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            //roles
            Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
            Set<RoleCreationForm> roleCreationFormSet = new HashSet<>();
            final int[] c = {0};
            roles.forEach(r -> {
                Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
                RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
                roleCreationFormSet.add(roleCreationForm);
                System.out.println("form " + Arrays.toString(c) + " "+ConvertToJson.setJsonString(roleCreationForm));
                c[0]++;
            });

            dataService.saveRolesList(roleCreationFormSet);

            //Users
            dataService.saveAUser(new NewUserForm("super admin", "superadmin", "superadmin@admin.com", "superadmin", AppRolesEnum.ROLE_ADMIN.name()));
            dataService.saveAUser(new NewUserForm("admin trainee", "trainee", "admin@trainee.com", "trainee", AppRolesEnum.ROLE_ADMIN_TRAINEE.name()));
            dataService.saveAUser(new NewUserForm("transporter", "transport", "jason@statham.com", "transport", AppRolesEnum.ROLE_TRANSPORTER.name()));
            dataService.saveAUser(new NewUserForm("certified", "certified", "certified@authority.com", "certified", AppRolesEnum.ROLE_CERTIFIED_AUTHORITY.name()));
            dataService.saveAUser(new NewUserForm("distributor", "distributor", "distributor@distribute.com", "distributor",  AppRolesEnum.ROLE_DISTRIBUTOR.name()));
            dataService.saveAUser(new NewUserForm("buyer", "buyer", "buyer@buyer.com", "buyer", AppRolesEnum.ROLE_BUYER.name()));
            dataService.saveAUser(new NewUserForm("seller", "seller", "seller@seller.com", "seller", AppRolesEnum.ROLE_SELLER.name()));



        };
    }

}
