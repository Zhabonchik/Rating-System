package org.leverx.ratingsystem.controller;

import jakarta.validation.Valid;
import org.leverx.ratingsystem.model.dto.*;
import org.leverx.ratingsystem.service.AuthenticationService;
import org.leverx.ratingsystem.utils.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authService;

    @Autowired
    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String signUp(@Valid @RequestBody CreateUserDto createUserDto) {
        return authService.create(createUserDto);
    }

    @PostMapping("/confirm")
    public String confirmEmail(@Valid @RequestBody ConfirmUserDto confirmUserDto) {
        return authService.confirmEmail(confirmUserDto);
    }

    @PostMapping("/login")
    public JwtResponse signIn(@Valid @RequestBody SignInUserDto signInUserDto) {
        return authService.verifyUser(signInUserDto);
    }

    @PostMapping("/forgot_password")
    public String verifyEmail(@Valid @RequestBody String email) {
        return authService.verifyEmail(email);
    }

    @PostMapping("/reset")
    public GetUserDto resetPassword(@Valid @RequestBody UpdateUserDto updateUserDto) {
        return authService.update(updateUserDto);
    }
}
