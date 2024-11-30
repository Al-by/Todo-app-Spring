package com.g3.libreriaestelar_pi.service;

import com.g3.libreriaestelar_pi.dto.ProyectoDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;

import java.util.List;

public interface ProyectoService {

    Proyecto crearProyecto(ProyectoDTO proyectoDTO, Long usuarioId);

    List<Proyecto> listarProyectosPorUsuario(Long usuarioId);

    Proyecto actualizarProyecto(Long id, ProyectoDTO proyectoDTO, Long usuarioId);

    void eliminarProyecto(Long id, Long usuarioId);
}
