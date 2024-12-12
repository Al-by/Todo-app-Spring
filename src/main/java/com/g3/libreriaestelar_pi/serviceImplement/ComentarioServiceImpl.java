package com.g3.libreriaestelar_pi.serviceImplement;

import com.g3.libreriaestelar_pi.model.Comentario;
import com.g3.libreriaestelar_pi.model.Tarea;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.repository.ComentarioRepository;
import com.g3.libreriaestelar_pi.repository.TareaRepository;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import com.g3.libreriaestelar_pi.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Comentario agregarComentario(Long tareaId, String contenido, Long usuarioId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setTarea(tarea);
        comentario.setUsuario(usuario);
        comentario.setContenido(contenido);

        return comentarioRepository.save(comentario);
    }

    @Override
    public List<Comentario> listarComentarios(Long tareaId) {
        return comentarioRepository.findByTareaId(tareaId);
    }

    @Override
    public Comentario editarComentario(Long comentarioId, String nuevoContenido, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        // Validar que el usuario autenticado sea el creador del comentario
        if (!comentario.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tiene permiso para editar este comentario");
        }

        // Actualizar el contenido del comentario
        comentario.setContenido(nuevoContenido);

        return comentarioRepository.save(comentario);
    }

    @Override
    public void eliminarComentario(Long comentarioId, Long usuarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        // Validar que el usuario autenticado sea el creador del comentario
        if (!comentario.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tiene permiso para eliminar este comentario");
        }

        // Eliminar el comentario
        comentarioRepository.delete(comentario);
    }
}
