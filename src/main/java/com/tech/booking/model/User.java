package com.tech.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.tech.booking.enums.Role;

@Entity
@Table (name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "first name is mandatory")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "last name is mandatory")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "email should be valid")
    @NotBlank(message = "email is mandatory")
    private String email;

    @Column(name = "login_id", nullable = false, unique = true)
    @NotBlank(message = "login id is mandatory")
    private String loginId;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "password is mandatory")
    private String password;

    @Column(name = "contact_number", nullable = false, unique = true)
    @NotBlank(message = "contact number is mandatory")
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // Default role is USER

}
