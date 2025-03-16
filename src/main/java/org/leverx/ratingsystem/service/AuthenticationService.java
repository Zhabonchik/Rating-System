package org.leverx.ratingsystem.service;

import org.leverx.ratingsystem.model.dto.*;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.utils.JwtResponse;
import org.springframework.stereotype.Service;

public interface AuthenticationService {

    public String create(CreateUserDto createUserDto);

    public String confirmEmail(ConfirmUserDto confirmUserDto);

    public JwtResponse verifyUser(SignInUserDto signInUserDto);

    public String verifyEmail(String email);

    public GetUserDto update(UpdateUserDto updateUserDto);

    public User getByEmail(String email);
}
