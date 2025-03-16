package org.leverx.ratingsystem.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(@NotNull @NotEmpty(message = "Must not be empty") @Email(message = "Invalid email format")String email,
                            @NotNull @NotEmpty(message = "Must not be empty") @Size(min = 8, message = "Must be at least 8 characters long") String password,
                            @NotNull @NotEmpty(message = "Must not be empty") String verificationCode) {
}
