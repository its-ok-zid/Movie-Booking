package com.tech.booking.service.impl;

import com.tech.booking.dto.ResetPasswordRequest;
import com.tech.booking.dto.UserDTO;
import com.tech.booking.enums.Role;
import com.tech.booking.exception.ResourceAlreadyExistsException;
import com.tech.booking.exception.ResourceNotFoundException;
import com.tech.booking.model.User;
import com.tech.booking.repository.UserRepository;
import com.tech.booking.utils.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestUserServiceImpl {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldRegisterUserSuccessfully() {
        UserDTO dto = new UserDTO();
        dto.setLoginId("user1");
        dto.setPassword("Password1@");
        dto.setEmail("user1@example.com");
        dto.setRole(Role.USER);

        when(userRepository.findByLoginId("user1")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserDTO result = userService.register(dto);

        assertThat(result.getLoginId()).isEqualTo("user1");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowIfUserExists() {
        UserDTO dto = new UserDTO();
        dto.setLoginId("user1");
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("User already exists");
    }

    @Test
    void login_shouldReturnTrueIfCredentialsMatch() {
        User user = new User();
        user.setLoginId("user1");
        user.setPassword("encoded");
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password1@", "encoded")).thenReturn(true);

        assertThat(userService.login("user1", "Password1@")).isTrue();
    }

    @Test
    void login_shouldReturnFalseIfCredentialsDoNotMatch() {
        User user = new User();
        user.setLoginId("user1");
        user.setPassword("encoded");
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThat(userService.login("user1", "wrong")).isFalse();
    }

    @Test
    void login_shouldReturnFalseIfUserNotFound() {
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.empty());
        assertThat(userService.login("user1", "Password1@")).isFalse();
    }

    @Test
    void forgotPassword_shouldReturnMaskedEmail() {
        User user = new User();
        user.setLoginId("user1");
        user.setEmail("john.doe@example.com");
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));

        String result = userService.forgotPassword("user1");
        assertThat(result).contains("jo****@example.com");
    }

    @Test
    void forgotPassword_shouldThrowIfUserNotFound() {
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.forgotPassword("user1"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with this login ID");
    }

    @Test
    void resetPassword_shouldResetSuccessfully() {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setLoginId("user1");
        req.setNewPassword("Password1@");
        req.setConfirmPassword("Password1@");

        User user = new User();
        user.setLoginId("user1");
        user.setPassword("oldEncoded");

        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password1@", "oldEncoded")).thenReturn(false);
        when(passwordEncoder.encode("Password1@")).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.resetPassword(req);
        assertThat(result).contains("Password reset successful for loginId: user1");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void resetPassword_shouldThrowIfPasswordInvalid() {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setLoginId("user1");
        req.setNewPassword("short");
        req.setConfirmPassword("short");

        // Simulate PasswordValidator.isValid returns false
        try (MockedStatic<PasswordValidator> validator = mockStatic(PasswordValidator.class)) {
            validator.when(() -> PasswordValidator.isValid("short")).thenReturn(false);

            assertThatThrownBy(() -> userService.resetPassword(req))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Password must be at least 8 characters");
        }
    }

    @Test
    void resetPassword_shouldThrowIfPasswordsDoNotMatch() {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setLoginId("user1");
        req.setNewPassword("Password1@");
        req.setConfirmPassword("Password2@");

        try (MockedStatic<PasswordValidator> validator = mockStatic(PasswordValidator.class)) {
            validator.when(() -> PasswordValidator.isValid("Password1@")).thenReturn(true);

            assertThatThrownBy(() -> userService.resetPassword(req))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("do not match");
        }
    }

    @Test
    void resetPassword_shouldThrowIfUserNotFound() {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setLoginId("user1");
        req.setNewPassword("Password1@");
        req.setConfirmPassword("Password1@");

        try (MockedStatic<PasswordValidator> validator = mockStatic(PasswordValidator.class)) {
            validator.when(() -> PasswordValidator.isValid("Password1@")).thenReturn(true);

            when(userRepository.findByLoginId("user1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.resetPassword(req))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found with this login ID");
        }
    }

    @Test
    void resetPassword_shouldThrowIfSameAsOldPassword() {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setLoginId("user1");
        req.setNewPassword("Password1@");
        req.setConfirmPassword("Password1@");

        User user = new User();
        user.setLoginId("user1");
        user.setPassword("encoded");

        try (MockedStatic<PasswordValidator> validator = mockStatic(PasswordValidator.class)) {
            validator.when(() -> PasswordValidator.isValid("Password1@")).thenReturn(true);

            when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("Password1@", "encoded")).thenReturn(true);

            assertThatThrownBy(() -> userService.resetPassword(req))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot be the same as the old password");
        }
    }
}