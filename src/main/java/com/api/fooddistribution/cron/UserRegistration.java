package com.api.fooddistribution.cron;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.config.security.AppRolesEnum;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedFullDate;

@Slf4j
@Component
public class UserRegistration {

    @Scheduled(fixedDelay = 5000, initialDelay = 10000) //every 10 seconds
    private void addRolesToUsers() {
        Page<Models.AppUser> usersWithoutRoles = userService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE));
        if (!usersWithoutRoles.isEmpty()) {
            //log.info("Checking user roles");
            usersWithoutRoles.forEach(u -> {
                if (u.getRole() == null) {
                    Models.AppRole role = userService.getARole(AppRolesEnum.ROLE_BUYER.name());
                    if (role != null) {
                        try {
                            u.setUpdatedAt(getNowFormattedFullDate());
                            userService.addARoleToAUser(u.getUsername(), role.getName());
                        } catch (ParseException | NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
