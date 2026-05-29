package com.barberia.resenas.exception;

import com.barberia.resenas.dto.CampoInvalidoDTO;
import com.barberia.resenas.dto.ErrorDTO;
import com.barberia.resenas.dto.ValidacionErrorDTO;
import org.springframework.dao.DataAccessException;
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> manejarResourceNotFoundException(ResourceNotFoundException ex) {
        return construirError(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidacionErrorDTO> manejarValidaciones(MethodArgumentNotValidException ex) {
        List<CampoInvalidoDTO> camposInvalidos = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> CampoInvalidoDTO.builder()
                        .campo(error.getField())
                        .mensaje(error.getDefaultMessage())
                        .build())
                .toList();

        ValidacionErrorDTO errorDTO = ValidacionErrorDTO.builder()
                .fecha(LocalDateTime.now())
                .estado(HttpStatus.BAD_REQUEST.value())
                .error("Error de validacion")
                .camposInvalidos(camposInvalidos)
                .build();

        return ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> manejarIllegalArgumentException(IllegalArgumentException ex) {
        return construirError(HttpStatus.BAD_REQUEST, "Error en la solicitud", ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDTO> manejarDataAccessException(DataAccessException ex) {
        return construirError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error de persistencia",
                "No fue posible procesar la operacion en la base de datos"
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> manejarRuntimeException(RuntimeException ex) {
        return construirError(HttpStatus.BAD_REQUEST, "Error en la solicitud", ex.getMessage());
    }

    private ResponseEntity<ErrorDTO> construirError(HttpStatus status, String error, String mensaje) {
        ErrorDTO errorDTO = new ErrorDTO(
                LocalDateTime.now(),
                status.value(),
                error,
                mensaje
        );

        return ResponseEntity.status(status).body(errorDTO);
    }
}
