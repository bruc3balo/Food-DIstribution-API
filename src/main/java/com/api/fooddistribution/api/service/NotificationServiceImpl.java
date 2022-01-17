package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.domain.Models.NotificationModels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.notificationRepo;


@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public NotificationModels saveNotification(NotificationModels notificationModels) {
        Long id = getNextNotificationId();
        notificationModels.setId(id);
        notificationModels.setDocumentId(String.valueOf(id));
        return notificationRepo.save(notificationModels);
    }

    @Override
    public List<NotificationModels> getUserNotifications(String uid) {
        return notificationRepo.retrieveAll().stream().filter(i -> i.getUid() != null && i.getUid().equals(uid)).collect(Collectors.toList());
    }

    @Override
    public boolean postNotification(NotificationModels notificationModels) {
        try {
            if (saveNotification(notificationModels) != null) {
                log.info("Notification has been posted successfully , {}",notificationModels.getUid());
                return true;
            } else {
                log.info("Failed to post notification , {}",notificationModels.getUid());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Failed to post notification , {}",notificationModels.getUid());
            return false;
        }
    }

    @Override
    public List<NotificationModels> getUnseenUserNotifications(String uid) {
        return notificationRepo.retrieveAll().stream().filter(i -> i.getUid() != null && i.getUid().equals(uid) && !i.isNotified()).collect(Collectors.toList());
    }

    @Override
    public void deleteAllNotifications() {
        notificationRepo.retrieveAll().forEach(n-> notificationRepo.remove(String.valueOf(n.getId())));
    }

    private Long getNextNotificationId() {
        final List<Long> ids = notificationRepo.retrieveAll().stream().map(Models.NotificationModels::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return ids.isEmpty() ? 1 : ids.get(0) + 1;
    }

}

