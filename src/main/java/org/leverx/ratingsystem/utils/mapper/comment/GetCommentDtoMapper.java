package org.leverx.ratingsystem.utils.mapper.comment;

import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class GetCommentDtoMapper {
    public static GetCommentDto toDto(Comment comment) {
        return new GetCommentDto(
                comment.getId(),
                comment.getMessage(),
                comment.getSeller().getId(),
                (comment.getAuthor() == null) ? null : comment.getAuthor().getId(),
                comment.getRating(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static List<GetCommentDto> toDto(List<Comment> comments) {
        return comments.stream()
                .map(GetCommentDtoMapper::toDto).collect(Collectors.toList());
    }

}
