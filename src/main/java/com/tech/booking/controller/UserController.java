package com.tech.booking.controller;

import com.tech.booking.dto.LoginRequest;
import com.tech.booking.dto.ResetPasswordRequest;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<?> login(@RequestParam("loginId") String loginId,
                                   @RequestParam("password") String password) {

        log.info("Login attempt for loginId: {}", loginId);
        Optional<com.tech.booking.model.User> userOpt = userService.login(loginId, password);

        if(userOpt.isPresent()) {
            com.tech.booking.model.User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role", user.getRole().name());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", "Invalid login credentials"));
        }
    }

    @Operation(summary = "Login using loginId and password (POST)")
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> loginPost(@Valid @RequestBody LoginRequest request) {

        log.info("Login POST attempt for loginId: {}", request.getLoginId());
        Optional<com.tech.booking.model.User> userOpt = userService.login(request.getLoginId(), request.getPassword());

        if (userOpt.isPresent()) {
            com.tech.booking.model.User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role", user.getRole().name());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", "Invalid login credentials"));
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

    @Operation(summary = "Reset password using loginId")
    @PostMapping(value = "/reset-password", consumes = "application/json")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        log.info("Reset password request for loginId: {}", request.getLoginId());
        String result = userService.resetPassword(request);
        return ResponseEntity.ok(result);
    }
}
