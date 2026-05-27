package com.barberia.barberos.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private LocalDateTime fecha;
    private Integer estado;
    private String error;
    private String mensaje;
}