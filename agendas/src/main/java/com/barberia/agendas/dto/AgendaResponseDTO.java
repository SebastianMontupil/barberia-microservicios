package com.barberia.agendas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaResponseDTO {

    private Long id;
    private Long clienteId;
    private Long barberoId;
    private Long servicioId;
    private LocalDateTime fechaHora;
    private String estado;
}
