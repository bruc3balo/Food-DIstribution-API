package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models.NotificationModels;

import java.text.ParseException;
import java.util.List;

public interface NotificationService {
    NotificationModels saveNotification(NotificationModels notificationModels) throws ParseException;
    List<NotificationModels> getUserNotifications (String uid);
    List<NotificationModels> getUnseenUserNotifications (String uid);

    boolean postNotification(NotificationModels notificationModels);
    void deleteAllNotifications();
}
