package org.leverx.ratingsystem.utils.mapper.comment;

import org.leverx.ratingsystem.model.dto.comment.CreateCommentDto;
import org.leverx.ratingsystem.model.entity.Comment;
import org.leverx.ratingsystem.model.entity.User;

public class CreateCommentDtoMapper {

    public static Comment toComment(CreateCommentDto createCommentDto, User seller, User author) {
        return Comment.builder()
                .message(createCommentDto.message())
                .author(author)
                .seller(seller)
                .rating(createCommentDto.rating())
                .verifiedByAdmin(false)
                .build();
    }
}
