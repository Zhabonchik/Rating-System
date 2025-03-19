package org.leverx.ratingsystem.service;

import org.leverx.ratingsystem.model.dto.email.EmailDto;
import org.leverx.ratingsystem.model.dto.user.*;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.utils.JwtResponse;

public interface AuthenticationService {

    String createUser(CreateUserDto createUserDto);

    String confirmEmail(ConfirmUserDto confirmUserDto);

    JwtResponse verifyUser(SignInUserDto signInUserDto);

    String verifyEmail(EmailDto emailDto);

    GetUserDto updateUser(UpdateUserDto updateUserDto);
}
