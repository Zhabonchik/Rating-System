package org.leverx.ratingsystem.utils.mapper.user;

import org.leverx.ratingsystem.model.dto.user.UpdateUserDto;
import org.leverx.ratingsystem.model.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UpdateUserDtoMapper {

    public static User toUser(User user, UpdateUserDto updateUserDto, BCryptPasswordEncoder passwordEncoder){
        user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        return user;
    }
}
