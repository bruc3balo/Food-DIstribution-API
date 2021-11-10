package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.config.FirestoreConfig;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.config.security.AppUserPermission;
import com.api.fooddistribution.utils.ConvertToJson;
import com.api.fooddistribution.utils.DataOps;
import com.google.cloud.firestore.CollectionReference;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.config.FirestoreConfig.firebaseAuth;
import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.*;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.google.firebase.auth.UserRecord.UpdateRequest;


@Service
@Slf4j
public class AuthServiceImp implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);

    /*  @Override
    public FirebaseSignInSignUpResponseBean authenticateUser(String email, String password) {
        return userAuthenticationServiceImpl.signInWithEmailAndPassword(email, password);
    }*/

    @Override
    public boolean isUserPresent(String username) {
        return userService.findByUsername(username).isPresent();
    }

    @Override
    public UserRecord authenticateNewUser(NewUserForm form) {

        try {
            if (!isUserPresent(form.getUsername())) {
                CreateRequest createRequest = new CreateRequest();

                createRequest.setDisabled(false);
                createRequest.setDisplayName(form.getUsername());
                createRequest.setEmail(form.getEmailAddress());
                createRequest.setPhoneNumber(form.getPhoneNumber());
                createRequest.setPassword(form.getPassword());
                createRequest.setEmailVerified(false);

                UserRecord record = firebaseAuth.createUser(createRequest);
                form.setUid(record.getUid());
                return record;
            } else {
                throw new DuplicateRequestException("user with name," + form.getUsername() + " already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" AUTH ERROR ::: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String sendPasswordChangeRequest(String email) throws FirebaseAuthException {
        return firebaseAuth.generatePasswordResetLink(email);
    }

    @Override
    public boolean deleteUser(String uid) {
        try {
            Models.AppUser user = userService.getAUserByUid(uid).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            firebaseAuth.deleteUser(user.getUid());
            return userService.deleteUser(user.getUid());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserRecord disableUser(String uid) {
        try {

            Models.AppUser user = userService.getAUserByUid(uid).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            userService.disableUser(user);

            UpdateRequest updateRequest = new UpdateRequest(user.getUid());
            updateRequest.setDisabled(true);
            return firebaseAuth.updateUser(updateRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public UserRecord enableUser(String uid) {
        try {

            Models.AppUser user = userService.getAUserByUid(uid).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            userService.enableUser(user);

            UpdateRequest updateRequest = new UpdateRequest(user.getUid());
            updateRequest.setDisabled(false);

            return firebaseAuth.updateUser(updateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String sendVerificationEmail(String email) {

        try {
            return firebaseAuth.generateEmailVerificationLink(email);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Models.AppUser isVerified(String email) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);

            if (userRecord.isEmailVerified()) {
                log.info("USER IS VERIFIED");
                return userService.updateAUser(userRecord.getUid(), new UserUpdateForm(true));
            } else {
                log.info("USER IS NOT VERIFIED");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void defaults() throws Exception {

        //permissions
       /* Set<String> newP = Arrays.stream(AppUserPermission.values()).map(AppUserPermission::getPermission).collect(Collectors.toSet());
        //userService.savePermissionList(newP);
        newP.forEach(p -> {
            Models.Permissions permissions = userService.saveAPermission(new Models.Permissions(DataOps.generatePermissionID(p), p));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Saved " + ConvertToJson.setJsonString(permissions));
        });


        //roles
        Set<String> roles = Arrays.stream(AppRolesEnum.values()).map(Enum::name).collect(Collectors.toSet());
        List<RoleCreationForm> roleCreationFormSet = new ArrayList<>();
        final int[] c = {0};
        roles.forEach(r -> {
            Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, r).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(r))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
            RoleCreationForm roleCreationForm = new RoleCreationForm(r, permissionsList);
            roleCreationFormSet.add(roleCreationForm);
            try {
                Models.AppRole updatedRole = userService.saveANewRole(roleCreationForm);

                log.info("updated" + ConvertToJson.setJsonString(updatedRole));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("form " + Arrays.toString(c) + " : " + roleCreationForm.getPermissions().size() + " :: " + ConvertToJson.setJsonString(roleCreationForm));
            c[0]++;
        });*/

       // System.out.println("role list " + roleCreationFormSet.size());
        //Users
        NewUserForm superAdminF = new NewUserForm("super admin", "superadmin", "superadmin@admin.com", "superadmin", "+254700000000", "1", "power", AppRolesEnum.ROLE_ADMIN.name());
        UserRecord superAdmin = authService.authenticateNewUser(superAdminF);
        if (superAdmin != null) {
            superAdminF.setUid(superAdmin.getUid());
            userService.saveAUser(superAdminF);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NewUserForm traineeF = new NewUserForm("admin trainee", "trainee", "admin@trainee.com", "trainee", "+254700000001", "2", "apprentice", AppRolesEnum.ROLE_ADMIN_TRAINEE.name());
        UserRecord trainee = authService.authenticateNewUser(traineeF);
        if (trainee != null) {
            traineeF.setUid(trainee.getUid());
            userService.saveAUser(traineeF);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NewUserForm transporterF = new NewUserForm("transporter", "transport", "jason@statham.com", "transport", "+254700000002", "3", "move", AppRolesEnum.ROLE_TRANSPORTER.name());
        UserRecord transporter = authService.authenticateNewUser(transporterF);
        if (transporter != null) {
            transporterF.setUid(transporter.getUid());
            userService.saveAUser(transporterF);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NewUserForm certifiedF = new NewUserForm("certified", "certified", "certified@authority.com", "certified", "+254700000003", "4", "help", AppRolesEnum.ROLE_DONOR.name());
        UserRecord certified = authService.authenticateNewUser(certifiedF);
        if (certified != null) {
            certifiedF.setUid(certified.getUid());
            userService.saveAUser(certifiedF);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NewUserForm distributorF = new NewUserForm("distributor", "distributor", "distributor@distribute.com", "distributor", "+254700000004", "5", "mature", AppRolesEnum.ROLE_DISTRIBUTOR.name());
        UserRecord distributor = authService.authenticateNewUser(distributorF);
        if (distributor != null) {
            distributorF.setUid(distributor.getUid());
            userService.saveAUser(distributorF);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NewUserForm buyerF = new NewUserForm("buyer", "buyer", "buyer@buyer.com", "buyerr", "+254700000005", "6", "chop", AppRolesEnum.ROLE_BUYER.name());
        UserRecord buyer = authService.authenticateNewUser(buyerF);
        if (buyer != null) {
            buyerF.setUid(buyer.getUid());
            userService.saveAUser(buyerF);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NewUserForm sellerF = new NewUserForm("seller", "seller", "seller@seller.com", "seller", "+254700000006", "7", "grind", AppRolesEnum.ROLE_SELLER.name());
        UserRecord seller = authService.authenticateNewUser(sellerF);

        if (seller != null) {
            sellerF.setUid(seller.getUid());
            userService.saveAUser(sellerF);
        }
        String teaImage = "https://images2.minutemediacdn.com/image/upload/c_fill,g_auto,h_1248,w_2220/v1555352925/shape/mentalfloss/istock_000059566150_small.jpg?itok=qh2qo4eB";
        String coffeeImage = "https://s-i.huffpost.com/gen/1693731/images/o-COFFEE-facebook.jpg";
        String tomatoesImage = "http://www.bhg.com.au/media/13840/170920-growing-tomatoes.jpg";
        String strawberries = "http://www.howtogrowstuff.com/wp-content/uploads/Strawberries1.jpg";
        String vegetable = "https://www.greenlife.co.ke/wp-content/uploads/2020/02/Cabbage.jpg";
        String beans = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi0.wp.com%2Fimages-prod.healthline.com%2Fhlcmsresource%2Fimages%2FAN_images%2Fkidney-beans-1296x728-feature.jpg%3Fw%3D1155%26h%3D1528&f=1&nofb=1";


//        //category
//        Models.ProductCategory beverage = productService.saveNewProductCategory("Beverage");
//        Thread.sleep(2000);
//        Models.ProductCategory vegetables = productService.saveNewProductCategory("Vegetables");
//        Thread.sleep(2000);
//        Models.ProductCategory fruits = productService.saveNewProductCategory("Fruits");
//        Thread.sleep(2000);
//        Models.ProductCategory proteins = productService.saveNewProductCategory("Proteins");
//        Thread.sleep(2000);
//        //product
//        Models.Product tea = productService.saveNewProduct(new ProductCreationFrom("tea", "100", "Beverage", teaImage,SOLID,"Bags of tea","seller"));
//        Thread.sleep(2000);
//        Models.Product coffee = productService.saveNewProduct(new ProductCreationFrom("coffee", "200",  "Beverage", coffeeImage,LIQUID,"Cups of coffee","seller"));
//        Thread.sleep(2000);
//        Models.Product tomatoes = productService.saveNewProduct(new ProductCreationFrom("tomatoes", "20", "Vegetables", tomatoesImage,SOLID,"A single tomatoe","seller"));
//        Thread.sleep(2000);
//        Models.Product strawberry = productService.saveNewProduct(new ProductCreationFrom("strawberries", "80", "Fruits", strawberries,SOLID,"a batch of strawberries","seller"));
//        Thread.sleep(2000);
//        Models.Product cabbage = productService.saveNewProduct(new ProductCreationFrom("cabbage", "150", "Vegetables", vegetable,SOLID,"A single calabash","seller"));
//        Thread.sleep(2000);
//        Models.Product bean = productService.saveNewProduct(new ProductCreationFrom("beans", "250", "Proteins", beans,SOLID,"A tray of beans","seller"));


    }

    @Override
    public void clearDb() {
        log.info("REMOVED ALL USERS");
        log.info("REMOVED ALL PERMISSIONS");
        log.info("REMOVED ALL ROLES");
    }
}
