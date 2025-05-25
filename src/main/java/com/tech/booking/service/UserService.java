package com.tech.booking.service;

import com.tech.booking.dto.UserDTO;

public interface UserService {

    UserDTO register(UserDTO userDTO);
    String login(UserDTO userDTO);
    String forgotPassword(String loginId);
}
