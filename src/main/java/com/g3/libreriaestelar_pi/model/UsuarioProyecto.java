package com.g3.libreriaestelar_pi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "usuario_proyecto")
public class UsuarioProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
}
