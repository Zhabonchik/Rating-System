package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.user.UserAlreadyExistsException;
import org.leverx.ratingsystem.exception.user.UserNotEnabledException;
import org.leverx.ratingsystem.exception.user.UserNotFoundException;
import org.leverx.ratingsystem.model.dto.email.EmailDto;
import org.leverx.ratingsystem.model.dto.user.*;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.model.entity.UserPrincipal;
import org.leverx.ratingsystem.repository.UserRepository;
import org.leverx.ratingsystem.service.AuthenticationService;
import org.leverx.ratingsystem.utils.mapper.user.CreateUserDtoMapper;
import org.leverx.ratingsystem.utils.mapper.user.GetUserDtoMapper;
import org.leverx.ratingsystem.utils.JwtResponse;
import org.leverx.ratingsystem.utils.mapper.user.UpdateUserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final EmailService emailService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
                                     JwtService jwtService, RedisService redisService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.redisService = redisService;
    }

    @Override
    public String createUser(CreateUserDto createUserDto) {

        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new UserAlreadyExistsException("User with email " + createUserDto.email() + " already exists");
        }

        User user = CreateUserDtoMapper.toUser(createUserDto, passwordEncoder);
        user.setEnabled(false);
        user.setVerifiedByAdmin(false);

        userRepository.save(user);

        sendVerificationCode(user.getEmail());

        return "Check email for verification token";
    }

    public String confirmEmail(ConfirmUserDto confirmUserDto) {

        User user = userRepository.findByEmail(confirmUserDto.email())
                .orElseThrow(() -> new UserNotFoundException("User with email " + confirmUserDto.email() + " doesn't exist"));

        String verificationCode = redisService.getVerificationCode(confirmUserDto.email());

        redisService.checkVerificationCode(verificationCode, confirmUserDto.verificationCode());

        user.setEnabled(true);

        userRepository.save(user);

        redisService.deleteVerificationCode(confirmUserDto.email());

        return "Email has been successfully confirmed";
    }

    @Override
    public GetUserDto updateUser(UpdateUserDto updateUserDto) {

        Optional<User> optionalUser = userRepository.findByEmail(updateUserDto.email());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email " + updateUserDto.email() + " doesn't exist");
        }

        String verificationCode = redisService.getVerificationCode(updateUserDto.email());

        redisService.checkVerificationCode(verificationCode, updateUserDto.verificationCode());

        User user = UpdateUserDtoMapper.toUser(optionalUser.get(), updateUserDto, passwordEncoder);

        return GetUserDtoMapper.toDto(userRepository.save(user));

    }

    public JwtResponse verifyUser(SignInUserDto signInUserDto) {

        UserDetails userDetails = getUserDetails(signInUserDto.email());

        User user = userRepository.findByEmail(signInUserDto.email()).get();

        checkEnabled(user);
        checkVerified(user);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), signInUserDto.password(), userDetails.getAuthorities()
                        )
                );

        if (authentication.isAuthenticated()) {
            return new JwtResponse(jwtService.generateToken(userDetails));
        }

        return new JwtResponse("Fail");
    }

    @Override
    public String verifyEmail(EmailDto emailDto) {
        Optional<User> optionalUser = userRepository.findByEmail(emailDto.email());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email " + emailDto.email() + " doesn't exist");
        }

        sendVerificationCode(emailDto.email());

        return "Code for changing password has been sent to your email";
    }

    private UserDetails getUserDetails(String email) {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    private void checkEnabled(User user) {
        if (!user.getEnabled()) {
            throw new UserNotEnabledException("This account hasn't been enabled. Check email for verification token");
        }
    }

    private void checkVerified(User user) {
        if (!user.getVerifiedByAdmin()) {
            throw new UserNotEnabledException("This account hasn't been verified by administrator");
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(100000));

    }

    private void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();

        redisService.saveVerificationCode(email, verificationCode);

        emailService.sendVerificationEmail(email, verificationCode);
    }
}
