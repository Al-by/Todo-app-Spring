package com.g3.libreriaestelar_pi.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "tarea")
public class Tarea {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 255)
    @NotBlank(message = "La descripción no puede estar vacía")
	private String descripcion;
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	private LocalDateTime fechaVencimiento; //permite registrar fecha y hora
	
	@Column(nullable = false, length = 50)
	private String prioridad;
	
	@Column(nullable = false, length = 50)
	private String estado;
	
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonBackReference // Evita la serialización recursiva del proyecto
    private Proyecto proyecto;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference // Evita la serialización recursiva del proyecto
    private Usuario usuario;
	

}
