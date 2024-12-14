package com.g3.libreriaestelar_pi.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 30) // NOT NULL, UNIQUE, longitud máxima
    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 6, max = 30, message = "El username debe tener entre 6 y 30 caracteres")
    private String username;

    @Column(nullable = false, unique = true, length = 50) // NOT NULL, UNIQUE, longitud máxima
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe proporcionar un email válido")
    private String email;

    @Column(nullable = false, length = 255) // NOT NULL, longitud máxima
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true, length = 8) // NOT NULL, UNIQUE, longitud fija
    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener exactamente 8 dígitos")
    private String dni;
}
