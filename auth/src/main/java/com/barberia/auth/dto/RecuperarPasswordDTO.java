package com.barberia.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecuperarPasswordDTO {

    private String email;
    private String nuevaPassword;
}