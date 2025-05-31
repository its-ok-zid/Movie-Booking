package com.tech.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Login ID is mandatory")
    private String loginId;

    @NotBlank(message = "New password is mandatory")
    private String newPassword;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;
}
