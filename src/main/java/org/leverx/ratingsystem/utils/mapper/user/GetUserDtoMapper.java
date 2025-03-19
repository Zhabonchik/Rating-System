package org.leverx.ratingsystem.utils.mapper.user;

import org.leverx.ratingsystem.model.dto.user.GetUserDto;
import org.leverx.ratingsystem.model.entity.User;

import java.util.List;

public class GetUserDtoMapper {

    public static GetUserDto toDto(User user) {
        return new GetUserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    public static List<GetUserDto> toDto(List<User> users) {
        return users.stream()
                .map(GetUserDtoMapper::toDto)
                .toList();
    }
}
