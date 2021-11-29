package com.api.fooddistribution.api.service;



import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.domain.Models.*;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.NotFoundException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserService {

    //User
    AppUser saveAUser(NewUserForm userForm) throws Exception; //working
    AppUser updateAUser(String uid, UserUpdateForm updateForm) throws Exception; //working
    AppUser deleteUser(AppUser user) throws NotFoundException; //works
    boolean deleteUser(String uid) throws NotFoundException; //works
    AppUser disableUser(AppUser user) throws NotFoundException; //works
    AppUser enableUser(AppUser user) throws NotFoundException; //works
    Optional<AppUser> getAUserByUid(String uid); //works
    Optional<AppUser> findByUsername(String username); //works
    List<AppUser> getAllUsers(); //works
    List<String> getAllUsernames(); //works
    List<String> getAllPhoneNumbers(); //works
    List<String> getAllEmails(); //works

    //Role
   // Models.AppRole saveARole(String name) throws NotFoundException; //works
    AppRole saveANewRole(RoleCreationForm form) throws Exception;
    AppRole saveADirectRole(AppRole appRole);
    Set<AppRole> saveRolesList(List<RoleCreationForm> creationForms); //works
    Optional<AppRole> getARoleById(String name); //works
    List<AppRole> getAllRoles(); //works
    Optional<AppRole> findByRoleName(String roleName); //works
    boolean deleteRole(String roleId);
    AppRole updateRole(AppRole appRole) throws ParseException, JsonProcessingException;
    Models.AppUser addARoleToAUser(String username, String roleName) throws Exception; //working


    //Permission
    Permissions saveAPermission(Permissions permissions); //works
    Set<Permissions> savePermissionList (Set<String> permissions);
    Optional<Permissions> getAPermission(String name); //works

    Optional<Permissions> findByPermissionName(String permissionName); //works
    Permissions updatePermission(Permissions permissions) throws ParseException, JsonProcessingException;
    List<Permissions> getAllPermissions(); //works
    AppRole addAPermissionToARole(String roleName, String permissionName) throws Exception; //works
    AppRole addPermissionListToARole(String roleName, Set<String> permissionName) throws Exception; //works


    Cart saveACart(Cart cart);
    List<Cart> getUserCarts(String userid);
    Optional<Cart> getCart(String cartId);
    Boolean deleteCart(String id);

}
