package org.leverx.ratingsystem.model.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentDto(
        //@NotNull @NotEmpty(message = "Must not be empty") Integer id,
        @NotNull @NotEmpty(message = "Must not be empty") String message,
        @NotNull @Min(1) @Max(5) Integer rating) {
}
