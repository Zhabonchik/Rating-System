package org.leverx.ratingsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.leverx.ratingsystem.model.entity.Comment;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.model.entity.UserPrincipal;
import org.leverx.ratingsystem.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Custom filter that is used, when a user wants to delete or update a comment.
 * It ensures, that a particular comment belongs to the user, who wants to modify/delete it.
 * **/

@Component
public class CommentOwnerFilter extends OncePerRequestFilter {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentOwnerFilter(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if ((method.equals("PATCH") || method.equals("DELETE")) && requestURI.matches("/users/\\d+/comments/\\d+")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal userDetails)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                return;
            }

            String[] pathParts = requestURI.split("/");
            Integer userId = Integer.parseInt(pathParts[2]);
            Integer commentId = Integer.parseInt(pathParts[4]);

            Optional<Comment> commentOptional = commentRepository.findByIdWithAuthor(commentId);

            if (commentOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Comment not found");
                return;
            }

            Comment comment = commentOptional.get();

            if (!comment.getAuthor().getId().equals(userId) || !userDetails.getId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
