package com.hospital.assistant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IntentDto {

    private Long id;

    private IntentName name;

    private String message;

    private RoleDto role;

    private PriorityName priority;

    public IntentDto(Long id, IntentName name, String message, RoleDto role, PriorityName priority) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.role = role;
        this.priority = priority;
    }
}
