package com.barberia.pagos.exception;

import com.barberia.pagos.dto.CampoInvalidoDTO;
import com.barberia.pagos.dto.ErrorDTO;
import com.barberia.pagos.dto.ValidacionErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidacionErrorDTO> manejarValidaciones(MethodArgumentNotValidException ex) {
        List<CampoInvalidoDTO> camposInvalidos = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> CampoInvalidoDTO.builder()
                        .campo(error.getField())
                        .mensaje(error.getDefaultMessage())
                        .valorRechazado(error.getRejectedValue() == null ? null : error.getRejectedValue().toString())
                        .build())
                .toList();

        ValidacionErrorDTO response = ValidacionErrorDTO.builder()
                .fecha(LocalDateTime.now())
                .estado(HttpStatus.BAD_REQUEST.value())
                .error("Error de validacion")
                .mensaje("Existen campos invalidos en la solicitud")
                .camposInvalidos(camposInvalidos)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> manejarResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDTO response = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> manejarRuntimeException(RuntimeException ex) {
        ErrorDTO response = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error en la solicitud",
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> manejarException(Exception ex) {
        ErrorDTO response = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ocurrio un error inesperado"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
