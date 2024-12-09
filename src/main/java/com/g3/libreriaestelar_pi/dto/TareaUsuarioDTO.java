package com.g3.libreriaestelar_pi.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TareaUsuarioDTO {

	private String descripcion;
    private LocalDateTime fechaVencimiento;
    private String prioridad;
    private Boolean activo;
    private Long proyectoId;
    private String proyectoNombre;
}
