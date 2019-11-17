package com.hospital.assistant.domain;

import com.hospital.assistant.model.ScheduleDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "schedules")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private UserEntity user;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    public static ScheduleDto toDto(ScheduleEntity se) {
        return new ScheduleDto(se.getStart(), se.getEnd());
    }
}
