package com.tech.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Login ID is mandatory")
    private String loginId;

    @NotBlank(message = "Password is mandatory")
    private String password;
}
