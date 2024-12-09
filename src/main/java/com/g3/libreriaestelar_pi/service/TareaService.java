package com.g3.libreriaestelar_pi.service;

import java.util.List;

import com.g3.libreriaestelar_pi.dto.TareaDTO;
import com.g3.libreriaestelar_pi.dto.TareaUsuarioDTO;
import com.g3.libreriaestelar_pi.model.Tarea;


public interface TareaService {
	
	
	Tarea crearTarea(TareaDTO tareaDTO, Long usuarioId);
	
	List<TareaUsuarioDTO> listarTareasPorUsuario(Long usuarioId);
	
	Tarea actualizarTarea(Long id, TareaDTO tareaDTO, Long UsuarioId);
	
	void eliminarTarea (Long id, Long usuarioId);
	
	
	List<Tarea> filtrarTareasPorPrioridad(String prioridad, Long usuarioId);
	
    List<Tarea> filtrarTareasPorEstado(String estado, Long usuarioId);

	


}
