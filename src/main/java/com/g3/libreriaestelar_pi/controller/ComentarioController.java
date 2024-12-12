package com.g3.libreriaestelar_pi.controller;

import com.g3.libreriaestelar_pi.dto.ComentarioDTO;
import com.g3.libreriaestelar_pi.model.Comentario;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import com.g3.libreriaestelar_pi.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario obtenerUsuarioLogueado() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping("/{tareaId}")
    public ResponseEntity<Comentario> agregarComentario(
            @PathVariable Long tareaId,
            @Valid @RequestBody ComentarioDTO comentarioDTO) {
        Long usuarioId = obtenerUsuarioLogueado().getId(); // Obtener el ID del usuario logueado
        Comentario comentario = comentarioService.agregarComentario(tareaId, comentarioDTO.getContenido(), usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentario);
    }

    @GetMapping("/{tareaId}")
    public ResponseEntity<List<Comentario>> listarComentarios(@PathVariable Long tareaId) {
        return ResponseEntity.ok(comentarioService.listarComentarios(tareaId));
    }

    @PutMapping("/{comentarioId}")
    public ResponseEntity<Comentario> editarComentario(
            @PathVariable Long comentarioId,
            @RequestBody ComentarioDTO comentarioDTO) {
        Long usuarioId = obtenerUsuarioLogueado().getId(); // Obtener el usuario autenticado
        Comentario comentario = comentarioService.editarComentario(comentarioId, comentarioDTO.getContenido(), usuarioId);
        return ResponseEntity.ok(comentario); // Devolver el comentario actualizado
    }

    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long comentarioId) {
        Long usuarioId = obtenerUsuarioLogueado().getId(); // Obtener el usuario autenticado
        comentarioService.eliminarComentario(comentarioId, usuarioId);
        return ResponseEntity.noContent().build(); // Devolver un código 204 No Content
    }

}
