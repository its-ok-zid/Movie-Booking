package com.tech.booking.service.impl;

import com.tech.booking.dto.UserDTO;
import com.tech.booking.repository.UserRepository;
import com.tech.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO register(UserDTO userDTO) {
        return null;
    }

    @Override
    public boolean login(String loginId, String password) {
        return false;
    }

    @Override
    public String forgotPassword(String loginId) {
        return "";
    }
}
