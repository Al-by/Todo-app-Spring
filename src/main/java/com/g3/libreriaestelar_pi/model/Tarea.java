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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name = "tarea")
public class Tarea {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 255)
	private String descripcion;
	
	private LocalDateTime fechaVencimiento; //permite registrar fecha y hora
	
	@Column(nullable = false, length = 50)
	private String prioridad;
	
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonBackReference // Evita la serialización recursiva del proyecto
    private Proyecto proyecto;
	

}
