package com.barberia.barberos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampoInvalidoDTO {

    private String campo;
    private String mensaje;
    private String valorRechazado;
}
