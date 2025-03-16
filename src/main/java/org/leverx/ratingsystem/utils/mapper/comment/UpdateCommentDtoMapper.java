package org.leverx.ratingsystem.utils.mapper.comment;

import org.leverx.ratingsystem.model.dto.comment.UpdateCommentDto;
import org.leverx.ratingsystem.model.dto.user.UpdateUserDto;
import org.leverx.ratingsystem.model.entity.Comment;

public class UpdateCommentDtoMapper {
    public static Comment toComment(Comment comment, UpdateCommentDto updateCommentDto) {
        comment.setMessage(updateCommentDto.message());
        comment.setRating(updateCommentDto.rating());
        return comment;
    }
}
