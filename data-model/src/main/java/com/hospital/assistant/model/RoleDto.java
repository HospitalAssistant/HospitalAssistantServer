package com.hospital.assistant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

    private Long id;

    private RoleName name;

    public RoleDto(Long id, RoleName name) {
        this.id = id;
        this.name = name;
    }
}
