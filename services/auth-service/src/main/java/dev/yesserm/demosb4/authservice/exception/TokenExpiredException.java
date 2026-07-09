package dev.yesserm.demosb4.authservice.exception;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("Token has expired");
    }
}
