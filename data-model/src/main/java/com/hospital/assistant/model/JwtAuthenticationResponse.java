package com.hospital.assistant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private final String tokenType = "Bearer";
    private String accessToken;
    private Set<RoleName> roles;

    public JwtAuthenticationResponse(String accessToken, Set<RoleName> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }
}
