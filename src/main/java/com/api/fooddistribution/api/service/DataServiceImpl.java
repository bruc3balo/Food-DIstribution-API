package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.NewUserForm;
import com.api.fooddistribution.api.model.RoleCreationForm;
import com.api.fooddistribution.api.model.UserUpdateForm;
import com.api.fooddistribution.api.specification.UserPredicate;
import com.api.fooddistribution.config.security.AppRolesEnum;
import com.api.fooddistribution.utils.DataOps;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.dataService;
import static com.api.fooddistribution.global.GlobalService.passwordEncoder;
import static com.api.fooddistribution.utils.DataOps.*;

@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService, UserDetailsService {


    //User
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Models.AppUser appUser = userRepo.findByUsername(username).orElse(null);

        if (appUser == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User {} found in db ", appUser.getUsername());
        }

        //add all authorities and permissions to list
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().getName()));
        appUser.getRole().getAllowedPermissions().forEach(p -> {
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
    public Models.AppUser saveAUser(NewUserForm newUserForm) throws ParseException, NotFoundException {

        Page<Models.AppUser> users = getAllUsers(new UserPredicate(newUserForm.getUsername()), PageRequest.of(0, 1));

        if (!users.isEmpty()) {
            if (users.getContent().get(0).getUsername().equals(newUserForm.getUsername())) {
                throw new DuplicateRequestException("User has already been created");
            }
        }


        Models.AppUser newUser = new Models.AppUser(newUserForm.getName(), newUserForm.getUsername(), newUserForm.getEmailAddress(), passwordEncoder.encode(newUserForm.getPassword()));
        newUser.setCreatedAt(getNowFormattedFullDate());
        newUser.setUpdatedAt(getNowFormattedFullDate());
        newUser.setDeleted(false);

        log.info("Saving new user {} to db", newUser.getUsername());

        Models.AppUser createdUser = userRepo.save(newUser);

        if (newUserForm.getRole() != null) {

            if (!newUserForm.getRole().isBlank()) {
                if (!newUserForm.getRole().isEmpty()) {
                    try {
                        if (createdUser.getUsername() != null) {
                            log.info("Now add role {} to user {}", newUserForm.getRole(), createdUser.getUsername());
                            Thread.sleep(0);
                            addARoleToAUser(createdUser.getUsername(), newUserForm.getRole());
                        }
                    } catch (NotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            log.info("No role for user " + createdUser.getUsername());
            try {
                if (createdUser.getUsername() != null) {
                    log.info("Now add role {} to user {}", newUserForm.getRole(), AppRolesEnum.ROLE_BUYER.name());
                    Thread.sleep(0);
                    addARoleToAUser(createdUser.getUsername(), newUserForm.getRole());
                }
            } catch (NotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }


        return newUser;
    }

    @Override
    public Models.AppUser updateAUser(String username, UserUpdateForm updateForm) throws NotFoundException, ParseException {
        Models.AppUser user = getAUser(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + ", not found");
        }

        if (updateForm != null) {

            if (updateForm.getRole() != null) {
                addARoleToAUser(user.getUsername(), updateForm.getRole());
            }

            if (updateForm.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateForm.getPassword()));
            }

            if (updateForm.getEmailAddress() != null) {
                user.setEmailAddress(updateForm.getEmailAddress());
            }

            if (updateForm.getName() != null) {
                user.setName(updateForm.getName());
            }

            user.setUpdatedAt(getNowFormattedFullDate());

        }

        return userRepo.save(user);
    }

    @Override
    public Models.AppUser getAUser(String username) {
        log.info("Fetching user {} ", username);
        Optional<Models.AppUser> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public Models.AppUser toggleUserDeletion(Models.AppUser user) {
        user.setDeleted(!user.isDeleted());
        return userRepo.save(user);
    }

    @Override
    public Page<Models.AppUser> getAllUsers(Specification<Models.AppUser> specification, PageRequest pageRequest) {
        log.info("Fetching all users");
        return userRepo.findAll(specification, pageRequest);
    }

    @Override
    public Page<Models.AppUser> getAllUsers(PageRequest pageRequest) {
        return userRepo.findAll(pageRequest);
    }

    //Role
    /*@Override
    public Models.AppRole saveARole(String name) {
        if (getARole(name) != null) {
            throw new DuplicateRequestException("Role has already been created");
        } else {
            log.info("Saving new role {} to db", name);
            return roleRepo.save(new Models.AppRole(name));
        }
    }*/

    @Override
    public Models.AppRole saveANewRole(RoleCreationForm form) throws NotFoundException {
        if (getARole(form.getName()) != null) {
            throw new DuplicateRequestException("Role has already been created");
        } else {
            log.info("Saving new role {} to db", form.getName());

            Models.AppRole role = roleRepo.save(new Models.AppRole(form.getName()));

            return addPermissionListToARole(role.getName(), form.getAllowedPermissions());
        }
    }

    @Override
    public Set<Models.AppRole> saveRolesList(Set<RoleCreationForm> creationForms) {

        Set<Models.AppRole> savedRoles = new HashSet<>();

        creationForms.forEach(f -> {
            try {
                Models.AppRole role = saveANewRole(f);
                if (role != null) {
                    savedRoles.add(role);
                }
            } catch (DuplicateRequestException e) {
                log.error("Role already added");
            } catch (NotFoundException e) {
                e.printStackTrace();
                log.error("Failed to add role " + f.getName());
            }
        });
        return savedRoles;
    }

    @Override
    public Models.AppRole getARole(String name) {
        log.info("Fetching role {} ", name);
        Optional<Models.AppRole> role = roleRepo.findByName(name);
        return role.orElse(null);
    }

    @Override
    public List<Models.AppRole> getAllRoles() {
        log.info("Fetching all roles");
        return roleRepo.findAll();
    }

    @Override
    public void addARoleToAUser(String username, String roleName) throws NotFoundException {
        Models.AppUser user = getAUser(username);
        Models.AppRole role = getARole(roleName);

        if (user == null) {
            throw new UsernameNotFoundException("User not found " + username);
        }

        if (role == null) {
            throw new NotFoundException("Role not found " + roleName);
        }

        if (user.getRole() != null) {

            if (user.getRole().getName().equals(role.getName())) {
                throw new DuplicateRequestException("User already has role " + role);
            }


            if (user.getRole().getAllowedPermissions().isEmpty()) { //check if permissions is empty
                try {
                    Set<String> permissionsList = Enum.valueOf(AppRolesEnum.class, role.getName()).getGrantedAuthorities().stream().filter(i -> !Objects.equals(i, DataOps.getGrantedAuthorityRole(role.getName()))).map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
                    addPermissionListToARole(role.getName(), permissionsList);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                //do nothing
                log.info("No changes made for " + username);
            }

        } else { //role doesn't exists
            log.info("Adding role {} to seller {}", role.getName(), user.getUsername()); //will save because @Transactional
        }

        user.setRole(role);
    }

    @Override
    public Models.Permissions saveAPermission(Models.Permissions permissions) {
        if (getAllPermissions().stream().map(Models.Permissions::getName).collect(Collectors.toList()).contains(permissions.getName())) {
            throw new DuplicateRequestException("Permission already in db");
        } else {
            return appPermissionRepo.save(permissions);
        }
    }

    @Override
    public Models.AppRole addPermissionListToARole(String roleName, Set<String> permissionList) throws NotFoundException {
        Models.AppRole role = getARole(roleName);
        if (role == null) {
            throw new NotFoundException("Role not found " + roleName);
        }

        //save permissions in db
        Set<Models.Permissions> permissionsExistingInDb = getAllPermissions().stream().filter(p -> permissionList.contains(p.getName())).collect(Collectors.toSet()); //filter out present roles// match names
        role.getAllowedPermissions().addAll(permissionsExistingInDb);

        //save new permissions to db
        Set<Models.Permissions> newPermissionsToAddT0List = new HashSet<>();
        try {
            permissionList.stream().filter(p -> !permissionsExistingInDb.contains(new Models.Permissions(p))).map(Models.Permissions::new).collect(Collectors.toSet()).forEach(p -> {
                Models.Permissions newPermission = saveAPermission(p);
                if (newPermission != null) {
                    newPermissionsToAddT0List.add(newPermission);
                }
            });
        } catch (DuplicateRequestException e) {
            log.error("Permission already in db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        role.getAllowedPermissions().addAll(newPermissionsToAddT0List);
        return role;
    }

    @Override
    public Models.AppRole addAPermissionToARole(String roleName, String permissionName) throws NotFoundException {
        Models.AppRole role = getARole(roleName);
        Models.Permissions permissions = getAPermission(permissionName);

        if (role == null) {
            throw new NotFoundException("Role not found " + roleName);
        }

        if (permissions == null) {
            throw new NotFoundException("Permission not found " + permissionName + " for role " + roleName);
        }

        if (role.getAllowedPermissions().contains(permissions)) {
            throw new DuplicateRequestException("Role " + roleName + " already has permission " + permissionName);
        }


        log.info("Adding permission {} to role {}", permissions.getName(), role.getName());
        role.getAllowedPermissions().add(permissions); //will save because @Transactional

        return role;
    }

    @Override
    public List<Models.Permissions> getAllPermissions() {
        return appPermissionRepo.findAll();
    }

    @Override
    public Models.Permissions getAPermission(String name) {
        return appPermissionRepo.findByName(name);
    }
}
