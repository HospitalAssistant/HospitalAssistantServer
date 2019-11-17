package com.hospital.assistant.domain;

import com.hospital.assistant.model.IntentDto;
import com.hospital.assistant.model.IntentName;
import com.hospital.assistant.model.PriorityName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "intents")
public class IntentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(unique = true)
    private IntentName name;

    @NotNull
    private String message;

    @ManyToOne
    @NotNull
    private RoleEntity role;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PriorityName priority;

    public static IntentDto toDto(IntentEntity ie) {
        return new IntentDto(ie.id, ie.name, ie.message, RoleEntity.toDto(ie.role), ie.priority);
    }
}
