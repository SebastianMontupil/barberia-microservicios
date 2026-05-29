package com.barberia.reportes.exception;

import com.barberia.reportes.dto.CampoInvalidoDTO;
import com.barberia.reportes.dto.ErrorDTO;
import com.barberia.reportes.dto.ValidacionErrorDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ManejadorErrores {

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

    @ExceptionHandler(ReporteProcessingException.class)
    public ResponseEntity<ErrorDTO> manejarReporteProcessingException(ReporteProcessingException ex) {
        ErrorDTO errorDTO = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error al procesar reporte",
                "No fue posible generar el reporte solicitado"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> manejarException(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno",
                "Ocurrio un error inesperado al procesar la solicitud"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}
