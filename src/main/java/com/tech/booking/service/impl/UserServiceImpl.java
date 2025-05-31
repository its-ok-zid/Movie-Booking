package com.tech.booking.service.impl;

import com.tech.booking.dto.ResetPasswordRequest;
import com.tech.booking.dto.UserDTO;
import com.tech.booking.enums.Role;
import com.tech.booking.exception.ResourceAlreadyExistsException;
import com.tech.booking.exception.ResourceNotFoundException;
import com.tech.booking.model.User;
import com.tech.booking.repository.UserRepository;
import com.tech.booking.service.UserService;
import com.tech.booking.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user by saving the user details in the database.
     * If a user with the same loginId already exists, it throws ResourceAlreadyExistsException.
     *
     * @param userDTO the user details to register
     * @return UserDTO containing the registered user's details
     */
    //apply password validation (pending)
    @Override
    public UserDTO register(UserDTO userDTO) {
        log.info("Registration user with loginId: {}", userDTO.getLoginId());

        if (userRepository.findByLoginId(userDTO.getLoginId()).isPresent()) {
            log.warn("User with loginId {} already exists", userDTO.getLoginId());
            throw new ResourceAlreadyExistsException("User already exists with this loginId");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : Role.USER);

        // Hash the password before saving
        String encryptedPassword = new BCryptPasswordEncoder(12).encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);

        User savedUser = userRepository.save(user);
        UserDTO response = new UserDTO();
        BeanUtils.copyProperties(savedUser, response);

        log.info("User registered successfully: {}", savedUser.getLoginId());
        return response;
    }

    /**
     * Logs in a user by checking the loginId and password.
     * Returns true if login is successful, false otherwise.
     */
    @Override
    public boolean login(String loginId, String password) {
        log.info("User login attempt with loginId: {}", loginId);

        // Check if user exists and password matches
        return userRepository.findByLoginId(loginId)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    log.info("User login successful for loginId: {}", loginId);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Login failed for loginId: {}", loginId);
                    return false;
                });
    }

    /**
     * Handles forgot password functionality by sending reset instructions to the user's email.
     * Masks the email address before returning the response.
     *
     * @param loginId the login ID of the user
     * @return a message indicating that reset instructions have been sent
     */
    @Override
    public String forgotPassword(String loginId) {

        log.info("Processing forgot password request for loginId: {}", loginId);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    log.warn("User not found with loginId: {}", loginId);
                    return new ResourceNotFoundException("User not found with this login ID: "+ loginId);
                });

        String maskedEmail = maskEmail(user.getEmail());

        log.info("Reset password instructions sent to: {}", maskedEmail);
        return "Reset password instructions have been sent to " + maskedEmail + ". Please check your email.";
    }


    // Masks the email address by replacing part of the username with asterisks.
    private String maskEmail(String email) {
        if (!StringUtils.hasText(email) || !email.contains("@")) return "unknown@example.com";
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        String maskedUsername = username.length() <= 2 ? username.charAt(0) + "*" : username.substring(0, 2) + "****";
        return maskedUsername + "@" + domain;
    }

    // Resets the user's password after validating the new password and confirming it.
    @Override
    public String resetPassword(ResetPasswordRequest request) {

        log.info("Resetting password for loginId: {}", request.getLoginId());

        if(!PasswordValidator.isValid(request.getNewPassword())) {
            log.warn("Password does not meet complexity requirements for loginId: {}", request.getLoginId());
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            log.warn("New password and confirm password do not match for loginId: {}", request.getLoginId());
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> {
                    log.warn("User not found with loginId: {}", request.getLoginId());
                    return new ResourceNotFoundException("User not found with this login ID: " + request.getLoginId());
                });

        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            log.warn("New password cannot be the same as the old password for loginId: {}", request.getLoginId());
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        log.info("Password reset successful for loginId: {}", request.getLoginId());
        return "Password reset successful for loginId: " + request.getLoginId();
    }

}