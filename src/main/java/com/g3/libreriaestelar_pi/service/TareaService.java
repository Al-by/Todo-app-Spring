package com.g3.libreriaestelar_pi.service;

import java.util.List;

import com.g3.libreriaestelar_pi.dto.TareaDTO;
import com.g3.libreriaestelar_pi.model.Tarea;


public interface TareaService {
	
	Tarea crearTarea(TareaDTO tareaDTO, Long proyectoId);

    List<Tarea> listarTareas();

	/*
	List<Tarea> listarTareasPorProyecto(Long proyectoId);

    Tarea actualizarTarea(Long id, TareaDTO tareaDTO, Long proyectoId);

    void eliminarTarea(Long id);
	 */

}
