package com.barberia.agendas.exception;

import com.barberia.agendas.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO manejarRuntimeException(RuntimeException ex) {

        return new ErrorDTO(
                LocalDateTime.now(),
                400,
                "Error en la solicitud",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO manejarValidaciones(MethodArgumentNotValidException ex) {

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return new ErrorDTO(
                LocalDateTime.now(),
                400,
                "Error de validación",
                mensaje
        );
    }
}