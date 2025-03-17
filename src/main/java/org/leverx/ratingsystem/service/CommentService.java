package org.leverx.ratingsystem.service;

import org.leverx.ratingsystem.model.dto.comment.CreateCommentDto;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.comment.UpdateCommentDto;
import org.leverx.ratingsystem.model.entity.Comment;

import java.util.List;

public interface CommentService {

    GetCommentDto createComment(CreateCommentDto dto, Integer sellerId, String jwtToken);

    GetCommentDto updateComment(UpdateCommentDto dto, Integer commentId);

    GetCommentDto getCommentById(Integer id);

    GetCommentDto getCommentByAuthorAndId(Integer authorId, Integer commentId);

    List<GetCommentDto> getCommentsBySellerId(Integer sellerId);

    List<GetCommentDto> getCommentsByAuthorId(Integer authorId);

    void deleteComment(Integer id);

    void checkVerified(Comment comment);
}
