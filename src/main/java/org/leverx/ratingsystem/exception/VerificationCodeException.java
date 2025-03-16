package org.leverx.ratingsystem.exception;

public class VerificationCodeException extends RuntimeException{
    public VerificationCodeException(String message){
        super(message);
    }
}
