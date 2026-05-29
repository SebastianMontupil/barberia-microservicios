package com.barberia.reportes.exception;

public class ReporteProcessingException extends RuntimeException {

    public ReporteProcessingException(String message) {
        super(message);
    }

    public ReporteProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
