package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.user.UserAlreadyExistsException;
import org.leverx.ratingsystem.exception.user.UserNotEnabledException;
import org.leverx.ratingsystem.exception.user.UserNotFoundException;
import org.leverx.ratingsystem.exception.user.UserNotVerifiedException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
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

        logger.info("Checking if user with email: {} already exists", createUserDto.email());
        if (userRepository.existsByEmail(createUserDto.email())) {
            logger.warn("User with email: {} already exists", createUserDto.email());
            throw new UserAlreadyExistsException("User with email " + createUserDto.email() + " already exists");
        }

        logger.info("Creating new user");
        User user = CreateUserDtoMapper.toUser(createUserDto, passwordEncoder);
        user.setEnabled(false);
        user.setVerifiedByAdmin(false);

        logger.info("Saving user");
        userRepository.save(user);

        logger.info("Trying to send verification code to email: {} to confirm it", user.getEmail());
        sendVerificationCode(user.getEmail());

        return "Check email for verification token";
    }

    public String confirmEmail(ConfirmUserDto confirmUserDto) {

        logger.info("Ensuring if user with email: {} exists", confirmUserDto.email());
        User user = userRepository.findByEmail(confirmUserDto.email())
                .orElseThrow(() -> new UserNotFoundException("User with email " + confirmUserDto.email() + " doesn't exist"));

        logger.info("Getting verification code from Redis for email: {}", confirmUserDto.email());
        String verificationCode = redisService.getVerificationCode(confirmUserDto.email());

        logger.info("Comparing verification code from Redis for email: {} and received code", confirmUserDto.email());
        redisService.checkVerificationCode(verificationCode, confirmUserDto.verificationCode());

        logger.info("Enabling user");
        user.setEnabled(true);

        logger.info("Saving user in repository");
        userRepository.save(user);

        logger.info("Deleting verification code from Redis");
        redisService.deleteVerificationCode(confirmUserDto.email());

        return "Email has been successfully confirmed";
    }

    @Override
    public GetUserDto updateUser(UpdateUserDto updateUserDto) {

        logger.info("Fetching user from Repository by email: {} to update", updateUserDto.email());
        Optional<User> optionalUser = userRepository.findByEmail(updateUserDto.email());

        if (optionalUser.isEmpty()) {
            logger.warn("User with email: {} doesn't exists", updateUserDto.email());
            throw new UserNotFoundException("User with email " + updateUserDto.email() + " doesn't exist");
        }

        logger.info("Getting verification code to update user from Redis for email: {}", updateUserDto.email());
        String verificationCode = redisService.getVerificationCode(updateUserDto.email());

        logger.info("Ensuring that verification code for email: {} and received code coincide", updateUserDto.email());
        redisService.checkVerificationCode(verificationCode, updateUserDto.verificationCode());

        logger.info("Updating user with email: {}", updateUserDto.email());
        User user = UpdateUserDtoMapper.toUser(optionalUser.get(), updateUserDto, passwordEncoder);

        logger.info("Returning Dto of updated user");
        return GetUserDtoMapper.toDto(userRepository.save(user));

    }

    public JwtResponse verifyUser(SignInUserDto signInUserDto) {

        logger.info("Getting User Details to verify user with email: {}", signInUserDto.email());
        UserDetails userDetails = getUserDetails(signInUserDto.email());

        logger.info("Fetching user from Repository with email: {}", signInUserDto.email());
        User user = userRepository.findByEmail(signInUserDto.email()).get();

        logger.info("Ensuring that user account has been enabled");
        checkEnabled(user);
        logger.info("Ensuring that user account has been verified");
        checkVerified(user);

        logger.info("Authenticating user with email: {}", user.getEmail());
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), signInUserDto.password(), userDetails.getAuthorities()
                        )
                );

        if (authentication.isAuthenticated()) {
            logger.info("User has been authenticated. Returning token");
            return new JwtResponse(jwtService.generateToken(userDetails));
        }

        logger.info("User hasn't been authenticated");
        return new JwtResponse("Fail");
    }

    @Override
    public String verifyEmail(EmailDto emailDto) {
        logger.info("Fetching user by email: {}", emailDto.email());
        Optional<User> optionalUser = userRepository.findByEmail(emailDto.email());

        if (optionalUser.isEmpty()) {
            logger.warn("User with email {} doesn't exist", emailDto.email());
            throw new UserNotFoundException("User with email " + emailDto.email() + " doesn't exist");
        }

        logger.info("Trying to send verification code to email: {} to verify it", emailDto.email());
        sendVerificationCode(emailDto.email());

        return "Code for changing password has been sent to your email";
    }

    private UserDetails getUserDetails(String email) {
        logger.info("Getting user details for email: {}", email);
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    private void checkEnabled(User user) {
        if (!user.getEnabled()) {
            logger.warn("User with ID: {} hasn't verified his email yet", user.getId());
            throw new UserNotEnabledException("This account hasn't been enabled. Check email for verification token");
        }
    }

    private void checkVerified(User user) {
        if (!user.getVerifiedByAdmin()) {
            logger.warn("User with ID: {} hasn't been verified by admin yet", user.getId());
            throw new UserNotVerifiedException("This account hasn't been verified by administrator");
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(100000));
    }

    private void sendVerificationCode(String email) {
        logger.info("Generating verification code for email: {}", email);
        String verificationCode = generateVerificationCode();

        logger.info("Saving verification code for email: {} in Redis", email);
        redisService.saveVerificationCode(email, verificationCode);

        logger.info("Sending verification code to email: {}", email);
        emailService.sendVerificationEmail(email, verificationCode);
    }
}
