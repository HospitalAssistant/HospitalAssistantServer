package com.hospital.assistant.controller;

import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v2.model.GoogleCloudDialogflowV2WebhookResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.hospital.assistant.domain.IntentEntity;
import com.hospital.assistant.domain.IntentNameWrapper;
import com.hospital.assistant.domain.NotificationEntity;
import com.hospital.assistant.domain.RoleEntity;
import com.hospital.assistant.domain.UserEntity;
import com.hospital.assistant.exception.ResourceNotFoundException;
import com.hospital.assistant.model.IntentName;
import com.hospital.assistant.model.RoleName;
import com.hospital.assistant.security.CurrentUser;
import com.hospital.assistant.security.UserPrincipal;
import com.hospital.assistant.service.FirebaseNotificationSender;
import com.hospital.assistant.service.IntentService;
import com.hospital.assistant.service.NotificationService;
import com.hospital.assistant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = AssistantController.SELF_LINK)
public class AssistantController {

    public static final String SELF_LINK = "/hospital/assistant";

    private static final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseNotificationSender firebaseNotificationSender;

    @Autowired
    private IntentService intentService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping(path = "/test",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> webhook(@RequestBody String request) throws IOException {

        GoogleCloudDialogflowV2WebhookRequest parse = jacksonFactory.createJsonParser(request).parse(
                GoogleCloudDialogflowV2WebhookRequest.class);
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jacksonFactory.createJsonGenerator(stringWriter);

        Optional<IntentEntity> intent = intentService.findByName(IntentName.valueOf(parse.getQueryResult()
                .getIntent()
                .getDisplayName()));

        IntentEntity intentEntity = intent.get();

        GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
        sendNotifications(intentEntity);

        response.setFulfillmentText(String.format(
                "Вашата заявка е приета. %s ще дойде скоро",
                intentEntity.getRole().getName()));
        jsonGenerator.serialize(response);
        jsonGenerator.flush();

        return ResponseEntity.ok(stringWriter.toString());
    }

    @PostMapping(path = "/patient", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity webhook(@RequestBody IntentNameWrapper intentName,
                                  @CurrentUser UserPrincipal currentUser) {

        Optional<IntentEntity> intent = intentService.findByName(intentName.getIntent());

        if (!intent.isPresent()) {
            throw new ResourceNotFoundException("intent", "name", intentName.getIntent());
        }

        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();

        IntentEntity intentEntity = intent.get();

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(user);
        notificationEntity.setIntent(intentEntity);
        notificationEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notificationService.save(notificationEntity);

        sendNotifications(intentEntity);

        return ResponseEntity.ok(String.format(
                "Вашата заявка е приета. %s ще дойде скоро",
                intentEntity.getRole().getName()));
    }

    private void sendNotifications(IntentEntity intentEntity) {
        CompletableFuture.runAsync(() -> {
            for (UserEntity userEntity : userService.findAll()) {
                List<RoleName> roleNames = userEntity.getRoles().stream()
                        .map(RoleEntity::getName).collect(Collectors.toList());
                if (EnumSet.copyOf(roleNames).contains(intentEntity.getRole().getName())) {
                    // send push notification to all such people
                    log.info("Sending intent {} to {}",
                            intentEntity.getMessage(),
                            userEntity.getEmail());
                    try {
                        firebaseNotificationSender.sendNotifications(userEntity, intentEntity);
                    } catch (FirebaseMessagingException e) {
                        log.error("Failed to send notification to account {}", userEntity.getEmail());
                    }
                    log.info(String.format("sent push notification to '%s'",
                            userEntity.getEmail()));
                }
            }
        });
    }
}
