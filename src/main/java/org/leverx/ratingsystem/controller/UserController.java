package org.leverx.ratingsystem.controller;

import jakarta.validation.Valid;
import org.leverx.ratingsystem.model.dto.comment.CreateCommentDto;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.comment.UpdateCommentDto;
import org.leverx.ratingsystem.model.dto.gameObject.CreateGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.GetGameObjectDto;
import org.leverx.ratingsystem.model.dto.gameObject.UpdateGameObjectDto;
import org.leverx.ratingsystem.model.dto.user.GetUserWithRatingDto;
import org.leverx.ratingsystem.service.CommentService;
import org.leverx.ratingsystem.service.GameObjectService;
import org.leverx.ratingsystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final CommentService commentService;
    private final GameObjectService gameObjectService;
    private final UserService userService;

    @Autowired
    public UserController(CommentService commentService, GameObjectService gameObjectService, UserService userService) {
        this.commentService = commentService;
        this.gameObjectService = gameObjectService;
        this.userService = userService;
    }

    /* Endpoint for getting all users sorted by avg rating */
    @GetMapping("/rating")
    public Page<GetUserWithRatingDto> getUsersSortedByRating(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        logger.info("Getting sellers sorted by rating");
        return userService.findAllSortedByRating(page, size);
    }

    /* Endpoint for getting all comments, that seller received */
    @GetMapping("/{userId}/received-comments")
    public List<GetCommentDto> getReceivedComments(@PathVariable Integer userId) {
        logger.info("Getting all received comments of user with ID: {}", userId);
        return commentService.getCommentsBySellerId(userId);
    }

    /* Endpoint for getting all comments left by user */
    @GetMapping("/{userId}/comments")
    public List<GetCommentDto> getCommentsLeftByUser(@PathVariable Integer userId) {
        logger.info("Getting all comments left by user with ID: {}", userId);
        return commentService.getCommentsByAuthorId(userId);
    }

    /* Endpoint for getting a particular comment left by user */
    @GetMapping("/{userId}/comments/{commentId}")
    public GetCommentDto getParticularComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        logger.info("Getting a particular comment with ID:{}, left by user with ID: {}", commentId, userId);
        return commentService.getCommentByAuthorAndId(userId, commentId);
    }

    /* Endpoint for getting all game objects that are being sold by particular user */
    @GetMapping("/{userId}/objects")
    public List<GetGameObjectDto> getGameObjects(@PathVariable Integer userId) {
        logger.info("Getting all game objects sold by user with ID: {}", userId);
        return gameObjectService.getObjectsBySellerId(userId);
    }

    /* Endpoint for getting a particular game object that is being sold by particular user */
    @GetMapping("/{userId}/objects/{objectId}")
    public GetGameObjectDto getGameObjectById(@PathVariable Integer userId, @PathVariable Integer objectId) {
        logger.info("Getting a particular game object with ID:{}, being sold by user with ID: {}", objectId, userId);
        return gameObjectService.getObjectByIdAndSellerId(objectId, userId);
    }

    /* Endpoint for leaving a comment about a particular seller */
    @PostMapping("/{userId}/comments")
    public GetCommentDto leaveComment(@PathVariable Integer userId, @RequestBody @Valid CreateCommentDto createCommentDto,
                                      @RequestHeader(value = "Authorization", required = false) String token) {
        logger.info("Leaving a comment about a seller with ID: {}", userId);
        return commentService.createComment(createCommentDto, userId, token);
    }

    /* Endpoint for a particular user to add a game object for sale */
    @PostMapping("/{userId}/objects")
    public GetGameObjectDto addGameObject(@PathVariable Integer userId, @RequestBody CreateGameObjectDto createGameObjectDto,
                                          @RequestHeader(value = "Authorization", required = true) String token) {
        logger.info("Adding new object with title: {} for user with ID: {}", createGameObjectDto.title(), userId);
        return gameObjectService.createObject(createGameObjectDto, userId, token);
    }

    /* Endpoint for a particular user to update his comment */
    @PatchMapping("/{userId}/comments/{commentId}")
    public GetCommentDto updateComment(@PathVariable Integer userId, @PathVariable Integer commentId,
                                       @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        logger.info("Updating a comment with ID: {}, left by user with ID: {}", commentId, userId);
        return commentService.updateComment(updateCommentDto, commentId);
    }

    /* Endpoint for a particular seller to update his game object */
    @PatchMapping("/{userId}/objects/{objectId}")
    public GetGameObjectDto updateGameObject(@PathVariable Integer userId, @PathVariable Integer objectId,
                                             @RequestBody UpdateGameObjectDto updateGameObjectDto) {
        logger.info("Updating a game object with ID: {}, being sold by user with ID: {}", objectId, userId);
        return gameObjectService.updateObject(updateGameObjectDto, objectId);
    }

    /* Endpoint for a particular user to delete his comment */
    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer commentId) {
        logger.info("Deleting a comment with ID: {}, left by user with ID: {}", commentId, userId);
        commentService.deleteComment(commentId);
    }

    /* Endpoint for a particular seller to delete his game object */
    @DeleteMapping("/{userId}/objects/{objectId}")
    public void deleteObject(@PathVariable Integer userId, @PathVariable Integer objectId) {
        logger.info("Deleting a game object with ID: {}, being sold by user with ID: {}", objectId, userId);
        gameObjectService.deleteObject(objectId);
    }
}
