package com.ensao.gi4.security.token;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String tokenNotFound) {
        super(tokenNotFound);
    }
}
