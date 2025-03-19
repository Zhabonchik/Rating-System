package org.leverx.ratingsystem.controller;

import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.user.GetUserDto;
import org.leverx.ratingsystem.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /* Endpoint for getting comments to be verified */
    @GetMapping("/comments/verify")
    public List<GetCommentDto> getCommentsToVerify() {
        logger.info("Fetching comments that need to be verified");
        return adminService.getCommentsToVerify();
    }

    /* Endpoint for verifying particular comment */
    @PostMapping("/comments/{commentId}/verify")
    public GetCommentDto verifyComment(@PathVariable Integer commentId) {
        logger.info("Verifying comment with ID: {}", commentId);
        return adminService.verifyComment(commentId);
    }

    /* Endpoint for getting users to be verified */
    @GetMapping("/users/verify")
    public List<GetUserDto> getUsersToVerify() {
        logger.info("Fetching users that need to be verified");
        return adminService.getUsersToVerify();
    }

    /* Endpoint for verifying particular user */
    @PostMapping("/users/{userId}/verify")
    public GetUserDto verifyUser(@PathVariable Integer userId) {
        logger.info("Verifying user with ID: {}", userId);
        return adminService.verifyUser(userId);
    }

}
