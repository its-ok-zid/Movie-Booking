package com.tech.booking.service;

import com.tech.booking.dto.ResetPasswordRequest;
import com.tech.booking.dto.UserDTO;

public interface UserService {

    UserDTO register(UserDTO userDTO);
    boolean login(String loginId, String password);
    String forgotPassword(String loginId);
    String resetPassword(ResetPasswordRequest request);
}
