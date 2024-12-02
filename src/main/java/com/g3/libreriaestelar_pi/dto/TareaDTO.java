package com.g3.libreriaestelar_pi.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TareaDTO {
	
    private String descripcion;
    private LocalDateTime fechaVencimiento;
    private String prioridad;
    private Long proyectoId;

}
