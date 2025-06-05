package com.ea.reservations.exception;

public class DomainException extends RuntimeException {
    private final DomainExceptionCode code;

    public DomainException(String message) {
        super(message);
        this.code = DomainExceptionCode.DOMAIN_ERROR;
    }

    public DomainException(String message, DomainExceptionCode code) {
        super(message);
        this.code = code;
    }
}
