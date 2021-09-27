package com.api.fooddistribution;

import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.config.security.AppUserPermission;
import com.api.fooddistribution.utils.ConvertToJson;
import com.api.fooddistribution.utils.DataOps;
import javassist.NotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.dataService;
import static com.api.fooddistribution.global.GlobalService.userService;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableTransactionManagement

public class FoodDistributionApplication {


    public static void main(String[] args) {
        SpringApplication.run(FoodDistributionApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            //permissions
            Set<String> newP = Arrays.stream(AppUserPermission.values()).map(AppUserPermission::getPermission).collect(Collectors.toSet());
            userService.savePermissionList(newP);
            Thread.sleep(2000);


            //roles
            Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
            List<RoleCreationForm> roleCreationFormSet = new ArrayList<>();
            final int[] c = {0};
            roles.forEach(r -> {
                Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
                RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
                roleCreationFormSet.add(roleCreationForm);
                try {
                    userService.saveANewRole(roleCreationForm);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("form " + Arrays.toString(c) + " : "+roleCreationForm.getAllowedPermissions().size() + " :: " + ConvertToJson.setJsonString(roleCreationForm));
                c[0]++;
            });

            System.out.println("role list "+roleCreationFormSet);

           // userService.saveRolesList(roleCreationFormSet);

            //Users
            userService.saveAUser(new NewUserForm("super admin", "superadmin", "superadmin@admin.com", "superadmin", AppRolesEnum.ROLE_ADMIN.name()));
            userService.saveAUser(new NewUserForm("admin trainee", "trainee", "admin@trainee.com", "trainee", AppRolesEnum.ROLE_ADMIN_TRAINEE.name()));
            userService.saveAUser(new NewUserForm("transporter", "transport", "jason@statham.com", "transport", AppRolesEnum.ROLE_TRANSPORTER.name()));
            userService.saveAUser(new NewUserForm("certified", "certified", "certified@authority.com", "certified", AppRolesEnum.ROLE_CERTIFIED_AUTHORITY.name()));
            userService.saveAUser(new NewUserForm("distributor", "distributor", "distributor@distribute.com", "distributor", AppRolesEnum.ROLE_DISTRIBUTOR.name()));
            userService.saveAUser(new NewUserForm("buyer", "buyer", "buyer@buyer.com", "buyer", AppRolesEnum.ROLE_BUYER.name()));
            userService.saveAUser(new NewUserForm("seller", "seller", "seller@seller.com", "seller", AppRolesEnum.ROLE_SELLER.name()));

            String teaImage = "https://images2.minutemediacdn.com/image/upload/c_fill,g_auto,h_1248,w_2220/v1555352925/shape/mentalfloss/istock_000059566150_small.jpg?itok=qh2qo4eB";
            String coffeeImage = "https://s-i.huffpost.com/gen/1693731/images/o-COFFEE-facebook.jpg";
            String tomatoesImage = "http://www.bhg.com.au/media/13840/170920-growing-tomatoes.jpg";
            String strawberries = "http://www.howtogrowstuff.com/wp-content/uploads/Strawberries1.jpg";
            String vegetable = "https://www.greenlife.co.ke/wp-content/uploads/2020/02/Cabbage.jpg";


            //category
            dataService.saveNewProductCategory("Beverage");
            dataService.saveNewProductCategory("Vegetables");
            dataService.saveNewProductCategory("Fruits");
            dataService.saveNewProductCategory("Proteins");


            //product
            dataService.saveNewProduct(new ProductCreationFrom("tea", "100", "Beverage", teaImage));
            dataService.saveNewProduct(new ProductCreationFrom("coffee", "200", "Beverage", coffeeImage));
            dataService.saveNewProduct(new ProductCreationFrom("tomatoes", "20", "Vegetables", tomatoesImage));
            dataService.saveNewProduct(new ProductCreationFrom("strawberries", "80", "Fruits", strawberries));
            dataService.saveNewProduct(new ProductCreationFrom("cabbage", "150", "Vegetables", vegetable));


        };
    }

}
