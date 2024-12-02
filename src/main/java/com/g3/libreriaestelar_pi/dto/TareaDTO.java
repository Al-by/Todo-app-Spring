package com.g3.libreriaestelar_pi.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TareaDTO {
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    private LocalDateTime fechaVencimiento;
    private String prioridad;
    private Long proyectoId;

}
