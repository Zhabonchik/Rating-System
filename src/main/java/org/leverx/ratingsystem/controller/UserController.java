package org.leverx.ratingsystem.controller;

import jakarta.validation.Valid;
import org.leverx.ratingsystem.model.dto.comment.CreateCommentDto;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.comment.UpdateCommentDto;
import org.leverx.ratingsystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CommentService commentService;

    @Autowired
    public UserController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{userId}/received-comments")
    public List <GetCommentDto> receivedComments(@PathVariable Integer userId) {
        return commentService.getCommentsBySellerId(userId);
    }

    @GetMapping("/{userId}/comments")
    public List<GetCommentDto> getCommentsLeftByUser(@PathVariable Integer userId) {
        return commentService.getCommentsByAuthorId(userId);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public GetCommentDto getParticularComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        return commentService.getCommentByAuthorAndId(userId, commentId);
    }

    @PostMapping("/{userId}/comments")
    public GetCommentDto leaveComments(@PathVariable Integer userId, @RequestBody @Valid CreateCommentDto createCommentDto,
                                       @RequestHeader(value = "Authorization", required = false) String token) {
        return commentService.createComment(createCommentDto, userId, token);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public GetCommentDto updateComments(@PathVariable Integer userId, @PathVariable Integer commentId,
                                        @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return commentService.updateComment(updateCommentDto, commentId);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

}
