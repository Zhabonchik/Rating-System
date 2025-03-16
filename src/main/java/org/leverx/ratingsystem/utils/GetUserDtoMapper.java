package org.leverx.ratingsystem.utils;

import org.leverx.ratingsystem.model.dto.GetUserDto;
import org.leverx.ratingsystem.model.entity.User;

public class GetUserDtoMapper {

    public static GetUserDto toDto(User user) {
        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
