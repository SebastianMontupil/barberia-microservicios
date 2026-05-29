package com.barberia.agendas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidacionErrorDTO {

    private LocalDateTime fecha;
    private Integer estado;
    private String error;
    private String mensaje;
    private List<CampoInvalidoDTO> camposInvalidos;
}
