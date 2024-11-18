package com.g3.libreriaestelar_pi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g3.libreriaestelar_pi.dto.UsuarioRegistroDTO;
import com.g3.libreriaestelar_pi.service.UsuarioService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO usuarioDTO) {
    	try {
            usuarioService.registrarUsuario(usuarioDTO);
            return ResponseEntity.ok("Usuario creado satisfactoriamente");
        } catch (IllegalArgumentException e) {
            // Devuelve un mensaje de error con código 400
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Devuelve un mensaje genérico de error con código 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }
}
