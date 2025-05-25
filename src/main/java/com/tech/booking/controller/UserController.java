package com.tech.booking.controller;

import com.tech.booking.dto.UserDTO;
import com.tech.booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1.0/moviebooking", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Handles user registration, login and password recovery")
@Slf4j
public class UserController {

    private final UserService userService;

    // Endpoint to register a new user
    @Operation(summary = "Register a new user")
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {

        log.info("Attempting to register user with loginId: {}", userDTO.getLoginId());
        UserDTO savedUser = userService.register(userDTO);
        log.info("User registered successfully with loginId: {}", savedUser.getLoginId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Endpoint to log in a user
    @Operation(summary = "Login using loginId and password")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {

        UserDTO requestedUser = new UserDTO();
        requestedUser.setLoginId(userDTO.getLoginId());
        requestedUser.setPassword(userDTO.getPassword());
        log.info("Attempting to log in user with loginId: {}", userDTO.getLoginId());
        String token = userService.login(requestedUser);
        log.info("User logged in successfully with loginId: {}", userDTO.getLoginId());
        return ResponseEntity.ok(token);
    }

    // Endpoint to handle forgot password flow
    @Operation(summary = "Forgot password flow")
    @GetMapping("/{username}/forgot")
    public ResponseEntity<String> forgotPassword(@PathVariable("username") String loginId) {

        log.info("Password recovery requested for loginId: {}", loginId);
        String message = userService.forgotPassword(loginId);
        log.info("Forgot password processed for loginId: {}", loginId);
        return ResponseEntity.ok(message);
    }

}
