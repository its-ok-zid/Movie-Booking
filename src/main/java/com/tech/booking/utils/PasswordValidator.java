package com.tech.booking.utils;

import java.util.regex.Pattern;

public class PasswordValidator {

    // Regex pattern for password: At least 1 upper, 1 lower, 1 digit, 1 special char, and 8+ chars
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    public static boolean isValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
