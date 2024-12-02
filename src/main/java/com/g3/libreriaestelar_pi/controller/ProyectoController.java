package com.g3.libreriaestelar_pi.controller;

import com.g3.libreriaestelar_pi.controller.UsuarioController.ErrorResponse;
import com.g3.libreriaestelar_pi.dto.ProyectoDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.service.ProyectoService;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario obtenerUsuarioLogueado() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping("/proyectos")
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        Usuario usuario = obtenerUsuarioLogueado();
        List<Proyecto> proyectos = proyectoService.listarProyectosPorUsuario(usuario.getId());
        return ResponseEntity.ok(proyectos);
    }

    @PostMapping("/proyectos")
    public ResponseEntity<Object> crearProyecto(@RequestBody ProyectoDTO proyectoDTO) {
        Usuario usuario = obtenerUsuarioLogueado();
        try {
            Proyecto proyecto = proyectoService.crearProyecto(proyectoDTO, usuario.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(proyecto);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/proyectos/{id}")
    public ResponseEntity<Proyecto> actualizarProyecto(@PathVariable Long id, @RequestBody ProyectoDTO proyectoDTO) {
        Usuario usuario = obtenerUsuarioLogueado();
        Proyecto proyectoActualizado = proyectoService.actualizarProyecto(id, proyectoDTO, usuario.getId());
        return ResponseEntity.ok(proyectoActualizado);
    }

    @DeleteMapping("/proyectos/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioLogueado();
        proyectoService.eliminarProyecto(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }
    
    public static class ErrorResponse {
        private String errorType;
        private String message;

        public ErrorResponse(String errorType, String message) {
            this.errorType = errorType;
            this.message = message;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    
}
