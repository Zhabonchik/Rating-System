package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.comment.CommentNotFoundException;
import org.leverx.ratingsystem.exception.user.UserNotFoundException;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.user.GetUserDto;
import org.leverx.ratingsystem.model.entity.Comment;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.repository.CommentRepository;
import org.leverx.ratingsystem.repository.UserRepository;
import org.leverx.ratingsystem.service.AdminService;
import org.leverx.ratingsystem.utils.mapper.comment.GetCommentDtoMapper;
import org.leverx.ratingsystem.utils.mapper.user.GetUserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GetCommentDto> getCommentsToVerify() {

        List<Comment> comments = commentRepository.findAllNotVerified();

        return GetCommentDtoMapper.toDto(comments);
    }

    @Override
    public GetCommentDto verifyComment(Integer commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id = " + commentId + " not found"));

        comment.setVerifiedByAdmin(true);

        commentRepository.save(comment);

        return GetCommentDtoMapper.toDto(comment);
    }

    @Override
    public List<GetUserDto> getUsersToVerify() {
        List<User> users = userRepository.findAllNotVerified();

        return GetUserDtoMapper.toDto(users);
    }

    @Override
    public GetUserDto verifyUser(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + userId + " not found"));

        user.setVerifiedByAdmin(true);

        userRepository.save(user);

        return GetUserDtoMapper.toDto(user);
    }
}
