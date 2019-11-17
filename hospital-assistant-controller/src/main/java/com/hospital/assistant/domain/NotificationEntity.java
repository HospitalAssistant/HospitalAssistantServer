package com.hospital.assistant.domain;

import com.hospital.assistant.model.NotificationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private IntentEntity intent;

    @NotNull
    @ManyToOne
    private UserEntity user;

    @Column(columnDefinition = "boolean default false", insertable = false)
    private Boolean accepted;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public static NotificationDto toDto(NotificationEntity ne) {
        return new NotificationDto(ne.id, IntentEntity.toDto(ne.intent), ne.accepted, ne.getCreatedAt());
    }
}
