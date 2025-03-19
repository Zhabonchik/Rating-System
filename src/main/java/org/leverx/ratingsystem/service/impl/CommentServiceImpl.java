package org.leverx.ratingsystem.service.impl;

import jakarta.transaction.Transactional;
import org.leverx.ratingsystem.exception.comment.CommentNotFoundException;
import org.leverx.ratingsystem.exception.comment.CommentNotVerifiedException;
import org.leverx.ratingsystem.exception.comment.WrongAuthorOfCommentException;
import org.leverx.ratingsystem.exception.user.UserNotFoundException;
import org.leverx.ratingsystem.model.dto.comment.CreateCommentDto;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.comment.UpdateCommentDto;
import org.leverx.ratingsystem.model.entity.Comment;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.repository.CommentRepository;
import org.leverx.ratingsystem.repository.UserRepository;
import org.leverx.ratingsystem.service.CommentService;
import org.leverx.ratingsystem.utils.mapper.comment.CreateCommentDtoMapper;
import org.leverx.ratingsystem.utils.mapper.comment.GetCommentDtoMapper;
import org.leverx.ratingsystem.utils.mapper.comment.UpdateCommentDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, JwtService jwtService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public GetCommentDto createComment(CreateCommentDto createCommentDto, Integer sellerId, String jwtToken) {

        String authorEmail = null;

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            logger.info("Extracting token");
            jwtToken = jwtToken.substring(7);
            logger.info("Extracting email from token");
            authorEmail = jwtService.extractUsername(jwtToken);
        }

        logger.info("Attempt to fetch comment's author from repository");
        User author = userRepository.findByEmail(authorEmail)
                .orElse(null);

        logger.info("Fetching seller from repository");
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id " + sellerId + " not found"));

        logger.info("Creating comment");
        Comment comment = CreateCommentDtoMapper.toComment(createCommentDto, seller, author);

        logger.info("Saving comment in repository and returning Dto");
        return GetCommentDtoMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public GetCommentDto updateComment(UpdateCommentDto updateCommentDto, Integer commentId) {

        logger.info("Fetching comment with ID: {} to update it", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));

        logger.info("Updating comment with ID: {}", comment);
        Comment updatedComment = UpdateCommentDtoMapper.toComment(comment, updateCommentDto);

        logger.info("Saving updated comment in repository and returning comment Dto");
        return GetCommentDtoMapper.toDto(commentRepository.save(updatedComment));
    }

    @Override
    public GetCommentDto getCommentById(Integer id) {

        logger.info("Fetching comment with ID: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));

        logger.info("Verifying comment with ID: {}", id);
        checkVerified(comment);

        logger.info("Returning comment Dto");
        return GetCommentDtoMapper.toDto(comment);
    }

    @Override
    public GetCommentDto getCommentByAuthorAndId(Integer authorId, Integer commentId) {

        logger.info("Fetching comment with ID: {} and with author with ID: {}", commentId, authorId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getAuthor() == null || !comment.getAuthor().getId().equals(authorId)) {
            logger.warn("Wrong author with ID:{} of comment with ID: {}", authorId, commentId);
            throw new WrongAuthorOfCommentException("Comment with id = " + commentId + " doesn't belong to user with id = " + authorId);
        }

        logger.info("Verifying comment with ID: {}", commentId);
        checkVerified(comment);

        logger.info("Returning comment Dto");
        return GetCommentDtoMapper.toDto(comment);
    }

    @Override
    @Transactional
    public List<GetCommentDto> getCommentsBySellerId(Integer sellerId) {

        logger.info("Fetching seller with ID: {} to get received comments", sellerId);
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id " + sellerId + " not found"));

        logger.info("Filtering verified received comments of seller with ID: {}", sellerId);
        List<Comment> receivedVerifiedComments = seller.getReceivedComments().stream()
                .filter(Comment::getVerifiedByAdmin).toList();

        logger.info("Returning Dtos of comments, received by seller with ID: {}", sellerId);
        return GetCommentDtoMapper.toDto(receivedVerifiedComments);
    }

    @Override
    public List<GetCommentDto> getCommentsByAuthorId(Integer authorId) {

        logger.info("Fetching author with ID: {} to get comments left by him", authorId);
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id " + authorId + " not found"));

        logger.info("Filtering verified comments left by user with ID: {}", authorId);
        List<Comment> verifiedComments = author.getComments().stream()
                .filter(Comment::getVerifiedByAdmin).toList();

        logger.info("Returning Dtos of comments left by user with ID: {}", author);
        return GetCommentDtoMapper.toDto(verifiedComments);
    }

    @Override
    public void deleteComment(Integer id) {
        logger.info("Deleting comment with ID: {}", id);
        commentRepository.deleteById(id);
    }

    @Override
    public void checkVerified(Comment comment) {
        if (!comment.getVerifiedByAdmin()) {
            logger.warn("Comment with ID: {} hasn't been verified by admin", comment.getId());
            throw new CommentNotVerifiedException("Comment with id = " + comment.getId() + " hasn't been verified yet");
        }
    }
}
