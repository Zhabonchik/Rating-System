package org.leverx.ratingsystem.controller;

import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.dto.user.GetUserDto;
import org.leverx.ratingsystem.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/comments/verify")
    public List<GetCommentDto> getCommentsToVerify() {
        return adminService.getCommentsToVerify();
    }

    @PostMapping("/comments/{commentId}/verify")
    public GetCommentDto verifyComment(@PathVariable Integer commentId) {
        return adminService.verifyComment(commentId);
    }

    @GetMapping("/users/verify")
    public List<GetUserDto> getUsersToVerify() {
        return adminService.getUsersToVerify();
    }

    @PostMapping("/users/{userId}/verify")
    public GetUserDto verifyUser(@PathVariable Integer userId) {
        return adminService.verifyUser(userId);
    }

}
