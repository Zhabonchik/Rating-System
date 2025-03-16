package org.leverx.ratingsystem.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ConfirmUserDto (
        @NotNull @NotEmpty(message = "Must not be empty") @Email(message = "Invalid email format") @Email(message = "Invalid email format") String email,
        @NotNull @NotEmpty(message = "Must not be empty") String verificationCode) {
}
