package org.leverx.ratingsystem.controller;

import jakarta.validation.Valid;
import org.leverx.ratingsystem.model.dto.email.EmailDto;
import org.leverx.ratingsystem.model.dto.user.*;
import org.leverx.ratingsystem.service.AuthenticationService;
import org.leverx.ratingsystem.utils.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authService;

    @Autowired
    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    /* Endpoint for submitting user credentials */
    @PostMapping("/register")
    public String signUp(@Valid @RequestBody CreateUserDto createUserDto) {
        logger.info("Attempt to register new user with email: {}", createUserDto.email());
        return authService.createUser(createUserDto);
    }

    /* Endpoint for confirmation of email with confirmation code */
    @PostMapping("/confirm")
    public String confirmUser(@Valid @RequestBody ConfirmUserDto confirmUserDto) {
        logger.info("Attempt to confirm new user with email: {}", confirmUserDto.email());
        return authService.confirmEmail(confirmUserDto);
    }

    /* Endpoint for logging in */
    @PostMapping("/login")
    public JwtResponse signIn(@Valid @RequestBody SignInUserDto signInUserDto) {
        logger.info("Attempt to login user with email: {}", signInUserDto.email());
        return authService.verifyUser(signInUserDto);
    }

    /* Endpoint to get reset code to email to change password */
    @PostMapping("/forgot-password")
    public String verifyEmail(@Valid @RequestBody EmailDto emailDto) {
        logger.info("Verifying email: {}", emailDto.email());
        return authService.verifyEmail(emailDto);
    }

    /* Endpoint for changing password with the help of received code */
    @PostMapping("/reset")
    public GetUserDto resetPassword(@Valid @RequestBody UpdateUserDto updateUserDto) {
        logger.info("Attempt to reset password for user with email: {}", updateUserDto.email());
        return authService.updateUser(updateUserDto);
    }
}
