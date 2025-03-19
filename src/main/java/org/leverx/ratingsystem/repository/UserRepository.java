package org.leverx.ratingsystem.repository;

import org.leverx.ratingsystem.model.dto.user.GetUserWithRatingDto;
import org.leverx.ratingsystem.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verifiedByAdmin = FALSE")
    List<User> findAllNotVerified();

    @Query("""
    SELECT new org.leverx.ratingsystem.model.dto.user.GetUserWithRatingDto(
        u.id, u.email, u.firstName, u.lastName, COALESCE(CAST(AVG(c.rating) as double), 0.0)
    )
    FROM User u
    LEFT JOIN Comment c ON u.id = c.seller.id
    WHERE u.verifiedByAdmin = TRUE AND u.role = 'ROLE_SELLER'
    GROUP BY u.id, u.email, u.firstName, u.lastName
    ORDER BY COALESCE(CAST(AVG(c.rating) as double), 0.0) DESC
    """)
    Page<GetUserWithRatingDto> findAllSellersSortedByRating(Pageable pageable);
}
