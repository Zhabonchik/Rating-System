package org.leverx.ratingsystem.service;

import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.user.GetUserDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AdminService {

    List<GetCommentDto> getCommentsToVerify();

    GetCommentDto verifyComment(Integer commentId);

    List<GetUserDto> getUsersToVerify();

    GetUserDto verifyUser(Integer userId);
}
