package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.user.UserNotFoundException;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.model.entity.UserPrincipal;
import org.leverx.ratingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger serviceLogger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        serviceLogger.info("Fetching user from repository with email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            serviceLogger.warn("User not found; email = {}", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }

        serviceLogger.info("Returning userPrincipal of user with ID: {}", user.get().getId());
        return new UserPrincipal(user.get());
    }
}
