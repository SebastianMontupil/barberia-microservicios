package com.barberia.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private String token;
}