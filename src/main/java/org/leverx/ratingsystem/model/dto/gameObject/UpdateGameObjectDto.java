package org.leverx.ratingsystem.model.dto.gameObject;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateGameObjectDto(
        @NotNull @NotEmpty(message = "Item title must not be empty") String title,
        @NotNull @NotEmpty(message = "Item description must not be empty") String text
) {
}
