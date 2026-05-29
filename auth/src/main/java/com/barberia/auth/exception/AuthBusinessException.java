package com.barberia.auth.exception;

public class AuthBusinessException extends RuntimeException {

    public AuthBusinessException(String mensaje) {
        super(mensaje);
    }
}
