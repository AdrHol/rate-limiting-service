package com.adrhol.rate_limiting_service.exceptions;

public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(String.format("Ip %s has exceeded requests limit.", message));
    }
}
