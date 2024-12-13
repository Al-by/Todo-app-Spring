package com.g3.libreriaestelar_pi.service;

import com.g3.libreriaestelar_pi.model.Comentario;

import java.util.List;

public interface ComentarioService {
    Comentario agregarComentario(Long tareaId, String contenido, Long usuarioId);
    List<Comentario> listarComentarios(Long tareaId, Long usuarioId);
    Comentario editarComentario(Long comentarioId, String nuevoContenido, Long usuarioId);
    void eliminarComentario(Long comentarioId, Long usuarioId);
}
