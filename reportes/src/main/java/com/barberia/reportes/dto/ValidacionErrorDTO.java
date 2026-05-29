package com.barberia.reportes.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidacionErrorDTO {

    private LocalDateTime fecha;
    private Integer estado;
    private String error;
    private List<CampoInvalidoDTO> camposInvalidos;
}
