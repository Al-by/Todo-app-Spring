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
    	validarDni(usuarioDTO.getDni());
        validarEmailUnico(usuarioDTO.getEmail());
        validarPassword(usuarioDTO.getPassword());

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setDni(usuarioDTO.getDni());

        return usuarioRepository.save(usuario);
    }
    
    private void validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{1,8}")) {
            throw new IllegalArgumentException("El DNI debe contener solo números y tener hasta 8 caracteres.");
        }
    }
    
    private void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
    }
    
    private void validarPassword(String password) {
        if (password == null || password.length() > 8 || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener entre 6 y 8 caracteres.");
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
