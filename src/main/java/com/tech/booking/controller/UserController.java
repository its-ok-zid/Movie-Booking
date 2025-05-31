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


    @Operation(summary = "Register a new user")
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {

        log.info("Attempting to register user with loginId: {}", userDTO.getLoginId());
        UserDTO savedUser = userService.register(userDTO);
        log.info("User registered successfully with loginId: {}", savedUser.getLoginId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    @Operation(summary = "Login using loginId and password")
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("loginId") String loginId,
                                        @RequestParam("password") String password) {

        log.info("Login attempt for loginId: {}", loginId);
        boolean success = userService.login(loginId, password);

        if (success) {
            log.info("Login successful for loginId: {}", loginId);
            return ResponseEntity.ok("Login successful");
        } else {
            log.warn("Login failed for loginId: {}", loginId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

    @Operation(summary = "Forgot password flow")
    @GetMapping("/{username}/forgot")
    public ResponseEntity<String> forgotPassword(@PathVariable("username") String loginId) {

        log.info("Password recovery requested for loginId: {}", loginId);
        String message = userService.forgotPassword(loginId);
        log.info("Forgot password processed for loginId: {}", loginId);
        return ResponseEntity.ok(message);
    }

}
