package com.g3.libreriaestelar_pi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ComentarioDTO {
    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;
}
