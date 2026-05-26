package com.barberia.reportes.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO {

    private String titulo;
    private String descripcion;
    private Integer totalRegistros;
    private Integer totalIngresos;
    private Double promedioCalificacion;
}