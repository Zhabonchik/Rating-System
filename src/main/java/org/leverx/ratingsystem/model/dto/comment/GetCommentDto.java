package org.leverx.ratingsystem.model.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record GetCommentDto(
        @NotNull @NotEmpty(message = "Must not be empty") String message,
        @NotNull @NotEmpty(message = "Must not be empty") Integer sellerId,
        @NotNull @NotEmpty(message = "Must not be empty") Integer author_id,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotNull @NotEmpty(message = "Must not be empty") Instant createdAt,
        @NotNull @NotEmpty(message = "Must not be empty") Instant updatedAt) {
}
