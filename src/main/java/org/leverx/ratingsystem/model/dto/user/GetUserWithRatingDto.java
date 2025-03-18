package org.leverx.ratingsystem.model.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GetUserWithRatingDto(
        @NotNull @NotEmpty Integer id,
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty String firstName,
        @NotNull @NotEmpty String lastName,
        @NotNull @NotEmpty Double avgRating
) {
}
