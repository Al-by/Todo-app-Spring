package com.g3.libreriaestelar_pi.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
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
	
	@Column(length = 255)
    @NotBlank(message = "La descripción no puede estar vacía")
	private String descripcion;
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
	private LocalDateTime fechaVencimiento; //permite registrar fecha y hora
	
	@Column(nullable = false, length = 50)
	private String prioridad;
	
	@Column(nullable = false)
	private Boolean activo = true;
	
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonBackReference // Evita la serialización recursiva del proyecto
    private Proyecto proyecto;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference // Evita la serialización recursiva del proyecto
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @ManyToOne
    @JoinColumn(name = "asignado_id")
    private Usuario asignado;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;


}
