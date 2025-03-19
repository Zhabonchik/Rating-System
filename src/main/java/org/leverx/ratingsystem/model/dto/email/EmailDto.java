package org.leverx.ratingsystem.model.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EmailDto(
        @NotNull @NotEmpty @Email(message = "Invalid email type") String email
) {
}
