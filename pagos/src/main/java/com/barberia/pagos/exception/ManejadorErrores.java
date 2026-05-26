package com.barberia.pagos.exception;

import com.barberia.pagos.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
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
}
