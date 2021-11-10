package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.utils.DataOps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.NotFoundException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.type.LatLngOrBuilder;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.config.FirestoreConfig.firebaseAuth;
import static com.api.fooddistribution.global.GlobalService.authService;
import static com.api.fooddistribution.global.GlobalVariables.HY;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedFullDate;
import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.passwordEncoder;
import static com.api.fooddistribution.utils.DataOps.*;


@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    //User
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Models.AppUser appUser = findByUsername(username).orElse(null);


        if (appUser == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User {} found in db ", appUser.getUsername());
        }

        //add all authorities and permissions to list
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().getName()));
        appUser.getRole().getPermissions().forEach(p -> {
            if (!authorities.contains(new SimpleGrantedAuthority(p.getName()))) {
                log.info("Adding permissions {} to role {}", appUser.getRole().getName(), p);
                authorities.add(new SimpleGrantedAuthority(p.getName()));
            } else {
                System.out.println("Role already has permission");
            }
        });


        log.info("authorities " + authorities);

        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    @Override
    public Optional<Models.AppUser> findByUsername(String username) {
        List<Models.AppUser> users = getAllUsers().stream().filter(p -> p.getUsername().equals(username)).collect(Collectors.toList());

        if (!users.isEmpty()) {
            return Optional.of(users.get(0));
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Models.AppUser saveAUser(NewUserForm newUserForm) throws Exception {

        List<Models.AppUser> users = getAllUsers().stream().filter(u -> u.getUsername().equals(newUserForm.getUsername())).collect(Collectors.toList());

        if (!users.isEmpty()) {
            if (users.stream().findFirst().get().getUsername().equals(newUserForm.getUsername())) {
                throw new DuplicateRequestException("User has already been created");
            }
        }

        Models.AppUser newUser = new Models.AppUser(newUserForm.getUid(), newUserForm.getName(), newUserForm.getUsername(), newUserForm.getIdNumber(), newUserForm.getEmailAddress(), newUserForm.getPhoneNumber(), passwordEncoder.encode(newUserForm.getPassword()), newUserForm.getBio(), HY, getNowFormattedFullDate().toString(), getNowFormattedFullDate().toString(), null, false, false, false, true, HY); //tochange

        log.info("Saving new user {} to db", newUser.getUsername());

        Models.AppUser createdUser = userRepo.save(newUser);

        if (newUserForm.getRole() != null && !newUserForm.getRole().isBlank() && !newUserForm.getRole().isEmpty()) {

            try {
                if (createdUser.getUsername() != null) {
                    log.info("Now add role {} to user {}", newUserForm.getRole(), createdUser.getUsername());
                    Thread.sleep(1000);
                    newUser = addARoleToAUser(createdUser.getUsername(), newUserForm.getRole());
                }
            } catch (NotFoundException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                String link = authService.sendVerificationEmail(newUserForm.getEmailAddress());
                if (link != null) {
                    log.info("SEND VERIFICATION EMAIL");
                } else {
                    log.info("FAILED TO SEND VERIFICATION EMAIL");
                }
            }
        } else {
            log.info("No role for user " + createdUser.getUsername());
            try {
                if (createdUser.getUsername() != null) {
                    log.info("Now add role {} to user {}", newUserForm.getRole(), newUserForm.getUsername());
                    Thread.sleep(1000);
                    newUser = addARoleToAUser(createdUser.getUsername(), AppRolesEnum.ROLE_BUYER.name());
                }
            } catch (NotFoundException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                String link = authService.sendVerificationEmail(newUserForm.getEmailAddress());
                if (link != null) {
                    log.info("SEND VERIFICATION EMAIL");
                } else {
                    log.info("FAILED TO SEND VERIFICATION EMAIL");
                }
            }
        }

        return userRepo.save(newUser);
    }

    private void sendVerificationEmail(String email) throws FirebaseAuthException {
        String link = firebaseAuth.generateEmailVerificationLink(email);
    }

    @Override
    public Models.AppUser updateAUser(String uid, UserUpdateForm updateForm) throws Exception {
        Models.AppUser user = getAUserByUid(uid).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(uid + ", not found");
        }

        if (updateForm != null) {

            if (updateForm.getRole() != null) {
                addARoleToAUser(user.getUsername(), updateForm.getRole());
            }

            if (updateForm.getName() != null) {
                user.setNames(updateForm.getName());

            }

            if (updateForm.getPhoneNumber() != null) {
                user.setPhoneNumber(updateForm.getPhoneNumber());
            }

            if (updateForm.getIdNumber() != null) {
                user.setIdNumber(updateForm.getIdNumber());
            }

            if (updateForm.getBio() != null) {
                user.setBio(updateForm.getBio());
            }

            if (updateForm.getTutorial() != null) {
                user.setTutorial(updateForm.getTutorial());
            }

            if (updateForm.getVerified() != null) {
                user.setVerified(updateForm.getVerified());
            }

            if (updateForm.getProfilePicture() != null) {
                UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(uid);
                updateRequest.setPhotoUrl(updateForm.getProfilePicture().equals(HY) ? null : updateForm.getProfilePicture());
                firebaseAuth.updateUser(updateRequest);
                user.setProfilePicture(updateForm.getProfilePicture());
            }

            user.setUpdatedAt(getNowFormattedFullDate().toString());
        }

        return userRepo.save(user);
    }

    @Override
    public Optional<Models.AppUser> getAUserByUid(String uid) {
        log.info("Fetching user {} ", uid);
        return userRepo.get(uid);
    }

    @Override
    public Models.AppUser deleteUser(Models.AppUser user) {
        user.setDeleted(true);
        return userRepo.save(user);
    }

    @Override
    public boolean deleteUser(String uid) throws NotFoundException {
        return userRepo.remove(uid);
    }

    @Override
    public Models.AppUser disableUser(Models.AppUser user) {
        user.setDisabled(true);
        return userRepo.save(user);
    }

    @Override
    public Models.AppUser enableUser(Models.AppUser user) {
        user.setDisabled(false);
        return userRepo.save(user);
    }

    @Override
    public List<Models.AppUser> getAllUsers() {
        return userRepo.retrieveAll();
    }


    @Override
    public List<String> getAllUsernames() {
        return userRepo.retrieveAll().stream().map(Models.AppUser::getUsername).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllEmails() {
        return userRepo.retrieveAll().stream().map(Models.AppUser::getEmailAddress).filter(Objects::nonNull).collect(Collectors.toList());
    }


    @Override
    public List<String> getAllPhoneNumbers() {
        return userRepo.retrieveAll().stream().map(Models.AppUser::getPhoneNumber).filter(Objects::nonNull).collect(Collectors.toList());
    }

    //Role
    @Override
    public Models.AppRole saveANewRole(RoleCreationForm form) throws Exception {

        Models.AppRole addedRole = findByRoleName(form.getName()).orElse(null);
        if (addedRole != null) {
            throw new DuplicateRequestException("Role has already been created");
        } else {
            log.info("Saving new role {} to db with permissions {}", form.getName(), form.getPermissions().size());

            Models.AppRole role = appRoleRepo.save(new Models.AppRole(generateRoleID(form.getName()), form.getName()));

            return addPermissionListToARole(role.getName(), form.getPermissions());
            //return role;
        }
    }

    @Override
    public Models.AppRole updateRole(Models.AppRole appRole) throws ParseException, JsonProcessingException {
        return appRoleRepo.save(appRole);
    }

    @Override
    public Models.AppRole saveADirectRole(Models.AppRole appRole) {
        return appRoleRepo.save(appRole);
    }

    @Override
    public boolean deleteRole(String id) {

        return appRoleRepo.remove(id);
    }

    @Override
    public Set<Models.AppRole> saveRolesList(List<RoleCreationForm> creationForms) {

        Set<Models.AppRole> savedRoles = new HashSet<>();

        creationForms.forEach(f -> {
            try {
                Thread.sleep(1000);
                Models.AppRole role = saveANewRole(f);
                if (role != null) {
                    savedRoles.add(role);
                    log.info("Role {} saved with permissions {}", role.getName(), role.getPermissions().size());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (DuplicateRequestException e) {
                log.error("Role already added");
            } catch (NotFoundException e) {
                e.printStackTrace();
                log.error("Failed to add role " + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return savedRoles;
    }

    @Override
    public Optional<Models.AppRole> getARoleById(String id) {
        log.info("Fetching role {} ", id);
        return appRoleRepo.get(id);
    }

    @Override
    public Optional<Models.AppRole> findByRoleName(String roleName) {
        List<Models.AppRole> roles = getAllRoles().stream().filter(p -> p.getName().equals(roleName)).collect(Collectors.toList());
        if (!roles.isEmpty()) {
            return Optional.of(roles.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Models.AppRole> getAllRoles() {
        log.info("Fetching all roles");
        return appRoleRepo.retrieveAll();
    }


    @Override
    public Models.AppUser addARoleToAUser(String username, String roleName) throws Exception {
        Models.AppUser user = findByUsername(username).orElse(null);
        Models.AppRole role = findByRoleName(roleName).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found " + username);
        }

        if (role == null) {
            throw new Exception("Role not found " + roleName);
        }

        //update role
        Models.AppRole finalRole = role;
        Set<String> hardCodedPermissionList = Enum.valueOf(AppRolesEnum.class, role.getName()).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(finalRole.getName()))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
        for (String permission : hardCodedPermissionList) {
            if (!role.getPermissions().stream().map(Models.Permissions::getName).collect(Collectors.toList()).contains(permission)) {
                role = addAPermissionToARole(role.getName(), permission);
            }
        }


        if (user.getRole() != null) { //user has a role
            if (user.getRole().getName().equals(role.getName())) {
                //if role is same
                //update role
                if (user.getRole().getPermissions().isEmpty()) { //check if permissions is empty
                    try {
                        role = addPermissionListToARole(role.getName(), hardCodedPermissionList);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (String permission : hardCodedPermissionList) {
                        if (!user.getRole().getPermissions().stream().map(Models.Permissions::getName).collect(Collectors.toList()).contains(permission)) {
                            role = addAPermissionToARole(role.getName(), permission);
                        }
                    }
                }
            }

        } else { //role doesn't exists
            log.info("Adding role {} to  {}", role.getName(), user.getUsername());
        }

        user.setRole(role);
        return userRepo.save(user);
    }

    @Override
    public Models.Permissions saveAPermission(Models.Permissions permissions) {

        Models.Permissions newPermission = findByPermissionName(permissions.getName()).orElse(null);

        if (newPermission != null) {
            throw new DuplicateRequestException("Permission already in db");
        } else {
            return permissionsRepo.save(permissions);
        }
    }

    @Override
    public Optional<Models.Permissions> findByPermissionName(String permissionName) {
        List<Models.Permissions> permissionsList = getAllPermissions().stream().filter(p -> p.getName().equals(permissionName)).collect(Collectors.toList());

        if (!permissionsList.isEmpty()) {
            return Optional.of(permissionsList.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<Models.Permissions> savePermissionList(Set<String> permissions) {
        Set<Models.Permissions> permissionsSet = new HashSet<>();
        permissions.forEach(p -> {
            Models.Permissions permission = new Models.Permissions(generatePermissionID(p), p);
            saveAPermission(permission);
            permissionsSet.add(permission);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return permissionsSet;
    }

    @Override
    public Models.AppRole addPermissionListToARole(String roleName, Set<String> permissionList) throws Exception {
        log.info("{} Permissions to add {}", roleName, permissionList.size());


        Models.AppRole role = findByRoleName(roleName).orElse(null);
        if (role == null) {
            throw new Exception("Role not found " + roleName);
        }

        List<Models.Permissions> allPermissions = getAllPermissions();

        //save permissions in db
        Set<Models.Permissions> permissionsExistingInDb = new HashSet<>(); //filter out present roles// match names
        Set<Models.Permissions> newAllowedPermissions = new HashSet<>();
        Set<Models.Permissions> rolePermissions = new HashSet<>();

        permissionList.forEach(p -> {
            if (allPermissions.stream().map(Models.Permissions::getName).collect(Collectors.toList()).contains(p)) {
                allPermissions.forEach(dbPermission -> {
                    if (dbPermission.getName().equals(p)) {
                        permissionsExistingInDb.add(dbPermission);
                        rolePermissions.add(dbPermission);
                        log.info("Adding {} to list ", dbPermission.getName());
                    }
                });
            } else {
                newAllowedPermissions.add(new Models.Permissions(generatePermissionID(p), p));
                log.info("Adding new {} to list ", p);

            }
        });

        log.info("{} Permissions in db {}", roleName, permissionsExistingInDb.size());

        //save new permissions to db
        final int[] i = {0};
        try {
            newAllowedPermissions.forEach(p -> {
                try {
                    Models.Permissions newPermission = saveAPermission(p);
                    i[0]++;
                    if (newPermission != null) {
                        rolePermissions.add(newPermission);
                        log.info("Adding permission {} to role {}", newPermission.getName(), roleName);
                    }
                } catch (DuplicateRequestException e) {
                    log.error("Permission already in db");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        role.setPermissions(rolePermissions.stream().toList());

        log.info("{} Permissions not in db {}", roleName, i[0]);
        return updateRole(role);
        //return role;
    }

    @Override
    public Models.AppRole addAPermissionToARole(String roleName, String permissionName) throws Exception {
        Models.AppRole role = getARoleById(roleName).orElse(null);
        Models.Permissions permissions = getAPermission(permissionName).orElse(null);

        if (role == null) {
            throw new Exception("Role not found " + roleName);
        }

        if (permissions == null) {
            throw new Exception("Permission not found " + permissionName + " for role " + roleName);
        }

        if (role.getPermissions().contains(permissions)) {
            throw new DuplicateRequestException("Role " + roleName + " already has permission " + permissionName);
        }


        log.info("Adding permission {} to role {}", permissions.getName(), role.getName());
        role.getPermissions().add(permissions);

        return appRoleRepo.save(role);
    }

    @Override
    public List<Models.Permissions> getAllPermissions() {
        return permissionsRepo.retrieveAll();
    }

    @Override
    public Optional<Models.Permissions> getAPermission(String id) {
        return permissionsRepo.get(id);
    }

    @Override
    public Models.Permissions updatePermission(Models.Permissions permissions) throws ParseException, JsonProcessingException {
        return permissionsRepo.save(permissions);
    }
}
