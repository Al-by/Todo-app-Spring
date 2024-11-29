package com.g3.libreriaestelar_pi.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre de la tarea no puede estar vacío")
    @Size(max = 100, message = "El nombre de la tarea no debe exceder los 100 caracteres")
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    private Date fechaVencimiento;

    @Column(length = 10)
    private String prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
}
