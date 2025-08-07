package com.tech.booking.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPasswordValidator {

    @Test
    void isValid_shouldReturnTrueForValidPassword() {
        assertThat(PasswordValidator.isValid("Abcdef1@")).isTrue();
        assertThat(PasswordValidator.isValid("StrongPass1!")).isTrue();
        assertThat(PasswordValidator.isValid("Qwerty9$")).isTrue();
    }

    @Test
    void isValid_shouldReturnFalseForInvalidPassword() {
        // Too short
        assertThat(PasswordValidator.isValid("Ab1@")).isFalse();
        // No uppercase
        assertThat(PasswordValidator.isValid("abcdef1@")).isFalse();
        // No lowercase
        assertThat(PasswordValidator.isValid("ABCDEF1@")).isFalse();
        // No digit
        assertThat(PasswordValidator.isValid("Abcdefg@")).isFalse();
        // No special character
        assertThat(PasswordValidator.isValid("Abcdef12")).isFalse();
        // Null or empty
        assertThat(PasswordValidator.isValid("")).isFalse();
        assertThat(PasswordValidator.isValid(null)).isFalse();
    }
}