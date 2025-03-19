package org.leverx.ratingsystem.service;

import org.leverx.ratingsystem.model.dto.user.GetUserWithRatingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<GetUserWithRatingDto> findAllSortedByRating(int page, int size);

}
