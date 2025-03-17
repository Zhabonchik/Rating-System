package org.leverx.ratingsystem.repository;

import org.leverx.ratingsystem.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.id = :commentId")
    Optional<Comment> findByIdWithAuthor(@Param("commentId") Integer commentId);

    @Query("SELECT c FROM Comment c WHERE c.verifiedByAdmin = FALSE")
    List<Comment> findAllNotVerified();
}
