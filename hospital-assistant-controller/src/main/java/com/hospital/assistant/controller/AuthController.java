package com.hospital.assistant.controller;

import com.hospital.assistant.domain.RoleEntity;
import com.hospital.assistant.domain.UserEntity;
import com.hospital.assistant.exception.AppException;
import com.hospital.assistant.model.ApiResponse;
import com.hospital.assistant.model.JwtAuthenticationResponse;
import com.hospital.assistant.model.RoleName;
import com.hospital.assistant.model.SignInRequest;
import com.hospital.assistant.model.SignUpRequest;
import com.hospital.assistant.security.JwtTokenProvider;
import com.hospital.assistant.service.RoleService;
import com.hospital.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = AuthController.SELF_LINK)
public class AuthController {

    public static final String SELF_LINK = "/api/auth";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserEntity user = userService.findByEmail(signInRequest.getEmail()).get();

        Set<RoleName> roles = user.getRoles().stream().map(re -> re.getName()).collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse("Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity(signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));

        RoleEntity userRole = roleService.findByName(RoleName.from(signUpRequest.getRole()))
                .orElseThrow(() -> new AppException("UserEntity RoleEntity not set."));

        user.setRoles(Collections.singleton(userRole));

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("UserEntity registered successfully"));
    }
}