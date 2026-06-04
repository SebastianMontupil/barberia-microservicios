package com.barberia.reportes.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private LocalDateTime fecha;
    private Integer estado;
    private String error;
    private String mensaje;
}
