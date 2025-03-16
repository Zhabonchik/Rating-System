package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.comment.CommentNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

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
            jwtToken = jwtToken.substring(7);
            authorEmail = jwtService.extractUsername(jwtToken);
        }

        User author = userRepository.findByEmail(authorEmail)
                .orElse(null);

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id " + sellerId + " not found"));

        Comment comment = CreateCommentDtoMapper.toComment(createCommentDto, seller, author);

        return GetCommentDtoMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public GetCommentDto updateComment(UpdateCommentDto updateCommentDto, Integer commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));

        Comment updatedComment = UpdateCommentDtoMapper.toComment(comment, updateCommentDto);

        return GetCommentDtoMapper.toDto(commentRepository.save(updatedComment));
    }

    @Override
    public GetCommentDto getCommentById(Integer id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));

        return GetCommentDtoMapper.toDto(comment);
    }

    @Override
    public GetCommentDto getCommentByAuthorAndId(Integer authorId, Integer commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getAuthor() == null || !comment.getAuthor().getId().equals(authorId)) {
            throw new WrongAuthorOfCommentException("Comment with id = " + commentId + " doesn't belong to user with id = " + authorId);
        }

        return GetCommentDtoMapper.toDto(comment);
    }

    @Override
    public List<GetCommentDto> getCommentsBySellerId(Integer sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id " + sellerId + " not found"));

        return GetCommentDtoMapper.toDto(seller.getReceivedComments());
    }

    @Override
    public List<GetCommentDto> getCommentsByAuthorId(Integer authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("Seller with id " + authorId + " not found"));

        return GetCommentDtoMapper.toDto(author.getComments());
    }

    @Override
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
