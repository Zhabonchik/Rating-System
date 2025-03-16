package org.leverx.ratingsystem.repository;

import org.leverx.ratingsystem.model.entity.Comment;
import org.springframework.data.repository.CrudRepository;


public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
