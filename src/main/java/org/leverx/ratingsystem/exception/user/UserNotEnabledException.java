package org.leverx.ratingsystem.exception.user;

public class UserNotEnabledException extends RuntimeException {
    public UserNotEnabledException(String message) {
        super(message);
    }
}
