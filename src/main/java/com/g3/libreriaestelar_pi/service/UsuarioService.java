package com.g3.libreriaestelar_pi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.g3.libreriaestelar_pi.dto.UsuarioRegistroDTO;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(UsuarioRegistroDTO usuarioDTO) {
        validarUsername(usuarioDTO.getUsername());
        validarDni(usuarioDTO.getDni());
        validarEmailUnico(usuarioDTO.getEmail());
        validarPassword(usuarioDTO.getPassword());

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setDni(usuarioDTO.getDni());

        return usuarioRepository.save(usuario);
    }

    private void validarUsername(String username) {
        if (username == null || username.length() > 30 || username.length() < 6) {
            throw new IllegalArgumentException("El username debe tener entre 6 a 30 caracteres");
        }
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El username ya está en uso.");
        }
    }
    
    private void validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener solo números y tener hasta 8 caracteres.");
        }
        if (usuarioRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("El DNI ya está en uso.");
        }
    }
    
    private void validarEmailUnico(String email) {
    	String regex = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|[a-zA-Z0-9.-]+\\.edu\\.pe)$";
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        if (email == null || !email.matches(regex)) {
            throw new IllegalArgumentException("Coloque un email aceptable.");
        }
    }
    
    private void validarPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener 8 o mas caracteres.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula.");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número.");
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial.");
        }
    }
}
