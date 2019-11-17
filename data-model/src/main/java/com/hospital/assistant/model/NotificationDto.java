package com.hospital.assistant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {

    private Long id;

    @NotNull
    private IntentDto intent;

    private Boolean accepted;

    private Long milliseconds;

    public NotificationDto(Long id, @NotNull IntentDto intent, Boolean accepted, Timestamp createdAt) {
        this.id = id;
        this.intent = intent;
        this.accepted = accepted;
        this.milliseconds = createdAt.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof NotificationDto)) {
            return false;
        }

        NotificationDto notificationDto = (NotificationDto) o;
        return id.equals(notificationDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
