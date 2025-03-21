package org.leverx.ratingsystem.utils.mapper.user;

import org.leverx.ratingsystem.model.dto.user.CreateUserDto;
import org.leverx.ratingsystem.model.entity.Role;
import org.leverx.ratingsystem.model.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUserDtoMapper {

    public static User toUser(CreateUserDto createUserDto, BCryptPasswordEncoder passwordEncoder){
        return User.builder()
                .firstName(createUserDto.firstName())
                .lastName(createUserDto.lastName())
                .email(createUserDto.email())
                .password(passwordEncoder.encode(createUserDto.password()))
                .role(Role.ROLE_SELLER)
                .build();
    }

}
