package org.leverx.ratingsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leverx.ratingsystem.model.dto.comment.GetCommentDto;
import org.leverx.ratingsystem.model.entity.Comment;
import org.leverx.ratingsystem.model.entity.User;
import org.leverx.ratingsystem.repository.CommentRepository;
import org.leverx.ratingsystem.repository.UserRepository;
import org.leverx.ratingsystem.service.CommentService;
import org.leverx.ratingsystem.service.impl.CommentServiceImpl;
import org.leverx.ratingsystem.service.impl.JwtService;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTests {

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    CommentService commentService;

    @BeforeEach
    void setupEnv(){
        commentService = new CommentServiceImpl(commentRepository, userRepository, jwtService);

        when(commentRepository.findById(1)).thenReturn(
                Optional.of(
                        Comment.builder()
                                .id(1)
                                .message("Some message")
                                .author(new User())
                                .seller(new User())
                                .verifiedByAdmin(true)
                                .rating(5)
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build())
        );
    }

    @Test
    void getCommentByIdShouldReturnGetCommentDto() {

        GetCommentDto comment = commentService.getCommentById(1);

        assertInstanceOf(GetCommentDto.class, comment);

    }

    @Test
    void getCommentByIdShouldReturnGetCommentDtoWithProperId() {

        GetCommentDto comment = commentService.getCommentById(1);

        assertEquals(1, comment.id());

    }

    @Test
    void getCommentByIdShouldReturnGetCommentDtoWithProperRating() {

        GetCommentDto comment = commentService.getCommentById(1);

        assertEquals(5, comment.rating());

    }

}
