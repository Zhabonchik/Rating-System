package org.leverx.ratingsystem.controller;

import jakarta.validation.Valid;
import org.leverx.ratingsystem.model.dto.comment.CreateCommentDto;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.comment.UpdateCommentDto;
import org.leverx.ratingsystem.model.dto.gameObject.CreateGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.GetGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.UpdateGameObjectDto;
import org.leverx.ratingsystem.service.CommentService;
import org.leverx.ratingsystem.service.GameObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CommentService commentService;
    private final GameObjectService gameObjectService;

    @Autowired
    public UserController(CommentService commentService, GameObjectService gameObjectService) {
        this.commentService = commentService;
        this.gameObjectService = gameObjectService;
    }

    @GetMapping("/{userId}/received-comments")
    public List <GetCommentDto> getReceivedComments(@PathVariable Integer userId) {
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

    @GetMapping("/{userId}/objects")
    public List<GetGameObjectDto> getGameObjects(@PathVariable Integer userId) {
        return gameObjectService.getObjectsBySellerId(userId);
    }

    @GetMapping("/{userId}/objects/{objectId}")
    public GetGameObjectDto getGameObjectById(@PathVariable Integer userId, @PathVariable Integer objectId) {
        return gameObjectService.getObjectByIdAndSellerId(objectId, userId);
    }

    @PostMapping("/{userId}/comments")
    public GetCommentDto leaveComment(@PathVariable Integer userId, @RequestBody @Valid CreateCommentDto createCommentDto,
                                       @RequestHeader(value = "Authorization", required = false) String token) {
        return commentService.createComment(createCommentDto, userId, token);
    }

    @PostMapping("/{userId}/objects")
    public GetGameObjectDto addGameObject(@PathVariable Integer userId, @RequestBody CreateGameObjectDto createGameObjectDto,
                                          @RequestHeader(value = "Authorization", required = true) String token) {
        return gameObjectService.createObject(createGameObjectDto, userId, token);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public GetCommentDto updateComment(@PathVariable Integer userId, @PathVariable Integer commentId,
                                        @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return commentService.updateComment(updateCommentDto, commentId);
    }

    @PatchMapping("/{userId}/objects/{objectId}")
    public GetGameObjectDto updateGameObject(@PathVariable Integer userId, @PathVariable Integer objectId,
                                             @RequestBody UpdateGameObjectDto updateGameObjectDto) {
        return gameObjectService.updateObject(updateGameObjectDto, objectId);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @DeleteMapping("/{userId}/objects/{objectId}")
    public void deleteObject(@PathVariable Integer userId, @PathVariable Integer objectId) {
        gameObjectService.deleteObject(objectId);
    }
}
