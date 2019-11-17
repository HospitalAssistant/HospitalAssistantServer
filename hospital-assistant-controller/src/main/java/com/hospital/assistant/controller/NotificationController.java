package com.hospital.assistant.controller;

import com.hospital.assistant.domain.NotificationEntity;
import com.hospital.assistant.domain.UserEntity;
import com.hospital.assistant.exception.ResourceNotFoundException;
import com.hospital.assistant.model.NotificationDto;
import com.hospital.assistant.model.RoleName;
import com.hospital.assistant.security.CurrentUser;
import com.hospital.assistant.security.UserPrincipal;
import com.hospital.assistant.service.NotificationService;
import com.hospital.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@CrossOrigin
@RestController
@RequestMapping(value = NotificationController.SELF_LINK)
public class NotificationController {

    public static final String SELF_LINK = "/notifications";

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResources<NotificationDto>> getNotifications(@CurrentUser UserPrincipal currentUser,
                                                                            @PageableDefault(value = 10) Pageable pageable,
                                                                            PagedResourcesAssembler assembler) {

        UserEntity user = userService.findByEmail(currentUser.getEmail()).get();

        List<RoleName> roleNames = user.getRoles().stream()
                .map(re -> re.getName())
                .collect(Collectors.toList());

        Page<NotificationEntity> notifications =
                notificationService.findAllNotAcceptedByRole(pageable, EnumSet.copyOf(roleNames));

        int totalElements = (int) notifications.getTotalElements();

        List<NotificationDto> notificationDtos = notifications.stream()
                .map(NotificationEntity::toDto)
                .collect(Collectors.toList());

        Page page = new PageImpl<>(notificationDtos, pageable, totalElements);

        PagedResources<NotificationDto> pagedResources;
        if (notificationDtos.isEmpty()) {
            pagedResources = assembler.toEmptyResource(page, NotificationDto.class);
        } else {
            pagedResources = assembler.toResource(page, linkTo(NotificationController.class).withSelfRel());
        }

        return new ResponseEntity<>(pagedResources, new HttpHeaders(), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{id:.+$}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public NotificationDto getNotification(@PathVariable("id") Long id) {

        Optional<NotificationEntity> oNotificationEntity = notificationService.findById(id);

        if (!oNotificationEntity.isPresent()) {
            throw new ResourceNotFoundException("notification", "id", id);
        }

        return NotificationEntity.toDto(oNotificationEntity.get());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PatchMapping(value = "/{id:.+$}/accept", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_NURSE','ROLE_SANITARY','ROLE_DOCTOR')")
    public void patchNotification(@PathVariable("id") Long id) {

        Optional<NotificationEntity> oNotificationEntity = notificationService.findById(id);

        if (!oNotificationEntity.isPresent()) {
            throw new ResourceNotFoundException("notification", "id", id);
        }

        NotificationEntity notificationEntity = oNotificationEntity.get();

        notificationEntity.setAccepted(true);
        notificationService.save(notificationEntity);
    }
}
