package com.api.fooddistribution.cron;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.config.security.AppRolesEnum;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedFullDate;

@Slf4j
@Component
public class UserRegistration {

    @Scheduled(fixedDelay = 5000, initialDelay = 20000) //every 10 seconds
    private void addRolesToUsers() {
        List<Models.AppUser> usersWithoutRoles = userService.getAllUsers();
        if (!usersWithoutRoles.isEmpty()) {
            //log.info("Checking user roles");
            usersWithoutRoles.forEach(u -> {
                if (u.getRole() == null) {
                    Models.AppRole role = userService.findByRoleName(AppRolesEnum.ROLE_BUYER.name()).orElse(null);
                    if (role != null) {
                        try {
                            u.setUpdatedAt(getNowFormattedFullDate().toString());
                            userService.addARoleToAUser(u.getUsername(), role.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
