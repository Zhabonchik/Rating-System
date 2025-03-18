package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.model.dto.user.GetUserWithRatingDto;
import org.leverx.ratingsystem.repository.UserRepository;
import org.leverx.ratingsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<GetUserWithRatingDto> findAllSortedByRating(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAllSortedByRating(pageable);
    }
}
