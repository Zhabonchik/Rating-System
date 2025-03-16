package org.leverx.ratingsystem.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.leverx.ratingsystem.model.entity.Role;

@Builder
public record GetUserDto (@NotNull @NotEmpty Integer id,
                          @NotNull @NotEmpty String email,
                          @NotNull @NotEmpty String firstName,
                          @NotNull @NotEmpty String lastName,
                          @NotNull @NotEmpty Role role) {
}
