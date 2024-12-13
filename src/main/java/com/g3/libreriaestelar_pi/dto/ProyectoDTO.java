package com.g3.libreriaestelar_pi.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ProyectoDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    private String descripcion;
    private Long usuarioId;
    private List<String> invitados; //IDs de usuarios invitados
}
