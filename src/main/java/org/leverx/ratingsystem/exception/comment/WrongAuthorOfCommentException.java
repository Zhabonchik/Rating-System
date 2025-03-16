package org.leverx.ratingsystem.exception.comment;

public class WrongAuthorOfCommentException extends RuntimeException {
    public WrongAuthorOfCommentException(String message) {
        super(message);
    }
}
