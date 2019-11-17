package com.hospital.assistant.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.hospital.assistant.domain.IntentEntity;
import com.hospital.assistant.domain.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@Slf4j
@Component
public class FirebaseNotificationSender {

    @Autowired
    public FirebaseNotificationSender() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(
                        "./hospital-assistant-controller/src/main/resources/fcmtestapp-bbc49-firebase-adminsdk-pg9xa-53441cbc46.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fcmtestapp-bbc49.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    public void sendNotifications(UserEntity userEntity, IntentEntity intentEntity) throws FirebaseMessagingException {
        if (userEntity.getFirebaseToken() == null) {
            log.debug("Cannot send notification to user account {} with roles {}. Cause: No firebase token",
                    userEntity.getEmail(),
                    userEntity.getRoles());
            return;
        }
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        if (userEntity.getSchedules().stream().anyMatch(scheduleEntity -> scheduleEntity.getStart()
                .isBefore(now) && scheduleEntity.getEnd().isAfter(now))) {
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(new Notification(intentEntity.getMessage(), ""))
                    .addAllTokens(Collections.singletonList(userEntity.getFirebaseToken()))
                    .build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            log.info(" {} messages were sent successfully to user {}", response.getSuccessCount(), userEntity.getEmail());
        }
    }

}
