package com.barberia.auth.exception;

import com.barberia.auth.dto.CampoInvalidoDTO;
import com.barberia.auth.dto.ErrorDTO;
import com.barberia.auth.dto.ValidacionErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class ManejadorErrores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidacionErrorDTO> manejarValidaciones(MethodArgumentNotValidException ex) {
        List<CampoInvalidoDTO> camposInvalidos = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> CampoInvalidoDTO.builder()
                        .campo(fieldError.getField())
                        .mensaje(fieldError.getDefaultMessage())
                        .valorRechazado(fieldError.getRejectedValue() == null
                                ? null
                                : String.valueOf(fieldError.getRejectedValue()))
                        .build())
                .toList();

        ValidacionErrorDTO error = ValidacionErrorDTO.builder()
                .fecha(LocalDateTime.now())
                .estado(HttpStatus.BAD_REQUEST.value())
                .error("Error de validacion")
                .mensaje("Existen campos invalidos en la solicitud")
                .camposInvalidos(camposInvalidos)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> manejarResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AuthBusinessException.class)
    public ResponseEntity<ErrorDTO> manejarAuthBusinessException(AuthBusinessException ex) {
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de negocio",
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> manejarRuntimeException(RuntimeException ex) {
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error en la solicitud",
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(error);
    }
}
