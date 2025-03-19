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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GetCommentDto> getCommentsToVerify() {
        logger.info("Fetching all not verified comments");
        List<Comment> comments = commentRepository.findAllNotVerified();

        logger.info("Returning Dtos of not verified comments");
        return GetCommentDtoMapper.toDto(comments);
    }

    @Override
    public GetCommentDto verifyComment(Integer commentId) {

        logger.info("Fetching not verified comment by ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id = " + commentId + " not found"));

        logger.info("Verifying comment with ID: {}", commentId);
        comment.setVerifiedByAdmin(true);

        logger.info("Saving verified comment with ID: {}", commentId);
        commentRepository.save(comment);

        logger.info("Returning Dto of verified comment");
        return GetCommentDtoMapper.toDto(comment);
    }

    @Override
    public List<GetUserDto> getUsersToVerify() {
        logger.info("Fetching all users to be verified");
        List<User> users = userRepository.findAllNotVerified();

        logger.info("Returning Dtos of users to verify");
        return GetUserDtoMapper.toDto(users);
    }

    @Override
    public GetUserDto verifyUser(Integer userId) {

        logger.info("Fetching not verified user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + userId + " not found"));

        logger.info("Verifying user with ID: {}", userId);
        user.setVerifiedByAdmin(true);

        logger.info("Saving verified user with ID: {}", userId);
        userRepository.save(user);

        logger.info("Returning Dto of verified user");
        return GetUserDtoMapper.toDto(user);
    }
}
