package com.tech.booking.dto;

import com.tech.booking.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Login ID is mandatory")
    private String loginId;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Password is mandatory")
    private String confirmPassword;

    @NotBlank(message = "Contact number is mandatory")
    private String contactNumber;

    private Role role;
}
