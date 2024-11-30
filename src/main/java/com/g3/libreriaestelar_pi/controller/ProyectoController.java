package com.g3.libreriaestelar_pi.controller;

import com.g3.libreriaestelar_pi.dto.ProyectoDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.service.ProyectoService;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Proyecto> crearProyecto(@RequestBody ProyectoDTO proyectoDTO) {
        Usuario usuario = obtenerUsuarioLogueado();
        Proyecto proyecto = proyectoService.crearProyecto(proyectoDTO, usuario.getId());
        return ResponseEntity.status(201).body(proyecto);
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
}
