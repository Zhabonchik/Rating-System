package org.leverx.ratingsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.leverx.ratingsystem.model.entity.GameObject;
import org.leverx.ratingsystem.model.entity.UserPrincipal;
import org.leverx.ratingsystem.repository.GameObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Custom filter that is used, when a user wants to delete or update a game object.
 * It ensures, that a particular game object belongs to the user, who wants to modify/delete it.
 * **/

@Component
public class GameObjectOwnerFilter extends OncePerRequestFilter {

    private final GameObjectRepository gameObjectRepository;

    @Autowired
    public GameObjectOwnerFilter(GameObjectRepository gameObjectRepository) {
        this.gameObjectRepository = gameObjectRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if ((method.equals("PATCH") || method.equals("DELETE")) && requestURI.matches("/users/\\d+/objects/\\d+")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal userDetails)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                return;
            }

            String[] pathParts = requestURI.split("/");
            Integer userId = Integer.parseInt(pathParts[2]);
            Integer objectId = Integer.parseInt(pathParts[4]);

            Optional<GameObject> objectOptional = gameObjectRepository.findById(objectId);
            if (objectOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Object not found");
                return;
            }

            GameObject gameObject = objectOptional.get();
            if (!gameObject.getUser().getId().equals(userId) || !userDetails.getId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
