package com.tech.booking.service;

import com.tech.booking.dto.ResetPasswordRequest;
import com.tech.booking.dto.UserDTO;
import com.tech.booking.model.User;

import java.util.Optional;

public interface UserService {
    UserDTO register(UserDTO userDTO);
    Optional<User> login(String loginId, String password);
    String forgotPassword(String loginId);
    String resetPassword(ResetPasswordRequest request);
}
