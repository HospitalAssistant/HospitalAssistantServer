package com.hospital.assistant.controller;

import com.hospital.assistant.domain.ScheduleEntity;
import com.hospital.assistant.domain.UserEntity;
import com.hospital.assistant.model.FirebaseRequest;
import com.hospital.assistant.model.ScheduleDto;
import com.hospital.assistant.security.CurrentUser;
import com.hospital.assistant.security.UserPrincipal;
import com.hospital.assistant.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/account")
public class ServerController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/updateToken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public ResponseEntity updateFirebaseToken(@RequestBody FirebaseRequest firebaseData,
                                              @CurrentUser UserPrincipal currentUser) {

        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();
        user.setFirebaseToken(firebaseData.getToken());

        userService.save(user);

        return ResponseEntity.ok("Successfully updated firebase token of " + user.getEmail());
    }

    @DeleteMapping(path = "/stopNotifications")
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public ResponseEntity stopNotificationsEndpoint(@CurrentUser UserPrincipal currentUser) {
        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();

        user.setFirebaseToken(null);
        userService.save(user);

        return ResponseEntity.ok("Successfully stopped notifications for user");
    }

    @GetMapping(path = "/workSchedule", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public ResponseEntity<List<ScheduleDto>> getSchedule(@CurrentUser UserPrincipal currentUser) {
        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();

        List<ScheduleDto> scheduleDtos = user.getSchedules().stream()
                .map(se -> ScheduleEntity.toDto(se)).collect(Collectors.toList());

        return ResponseEntity.ok(scheduleDtos);
    }

    @PostMapping(path = "/workSchedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public ResponseEntity addToWorkSchedule(@RequestBody ScheduleDto scheduleDto,
                                            @CurrentUser UserPrincipal currentUser) {

        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();

        ScheduleEntity se = new ScheduleEntity();
        se.setStart(scheduleDto.getStart());
        se.setEnd(scheduleDto.getEnd());
        se.setUser(user);

        user.getSchedules().add(se);
        userService.save(user);

        return ResponseEntity.ok("Updated work schedule successfully");
    }

    @GetMapping(path = "/workSchedule/current", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public String getCurrentShift(@CurrentUser UserPrincipal currentUser) {

        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();

        LocalDateTime now = LocalDateTime.now();
        Optional<ScheduleEntity> optionalIntervalDto = user.getSchedules().stream()
                .filter(intervalDto -> intervalDto.getStart().isBefore(now) && intervalDto.getEnd().isAfter(now)).findFirst();

        if (!optionalIntervalDto.isPresent()) {
            return "В момента не си на смяна";
        }

        LocalDateTime shiftEnd = optionalIntervalDto.get().getEnd();
        return String.format("Смяната ти свършва на: %d.%d %d:%d ",
                shiftEnd.getDayOfMonth(),
                shiftEnd.getMonthValue(),
                shiftEnd.getHour(),
                shiftEnd.getMinute());
    }
}
