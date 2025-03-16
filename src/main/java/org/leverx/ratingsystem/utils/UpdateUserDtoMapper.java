package org.leverx.ratingsystem.utils;

import org.leverx.ratingsystem.model.dto.UpdateUserDto;
import org.leverx.ratingsystem.model.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UpdateUserDtoMapper {

    public static User updateUser(User user, UpdateUserDto updateUserDto, BCryptPasswordEncoder passwordEncoder){
        user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        return user;
    }
}
