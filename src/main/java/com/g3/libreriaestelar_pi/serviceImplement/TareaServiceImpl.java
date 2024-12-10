package com.g3.libreriaestelar_pi.serviceImplement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g3.libreriaestelar_pi.dto.ProyectoDTO;
import com.g3.libreriaestelar_pi.dto.TareaDTO;
import com.g3.libreriaestelar_pi.dto.TareaUsuarioDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;
import com.g3.libreriaestelar_pi.model.Tarea;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.repository.ProyectoRepository;
import com.g3.libreriaestelar_pi.repository.TareaRepository;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import com.g3.libreriaestelar_pi.service.TareaService;

@Service
public class TareaServiceImpl implements TareaService {

	@Autowired
	private TareaRepository tareaRepository;
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	
	
	@Override
	public Tarea crearTarea(TareaDTO tareaDTO, Long usuarioId) {
		// Validar que el nombre de la tarea sea único para el usuario
		validarNombreTareaUnico(tareaDTO.getDescripcion(), tareaDTO.getProyectoId());
		
		// Obtener el usuario por ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Obtener el proyecto por ID
        Proyecto proyecto = proyectoRepository.findById(tareaDTO.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        if (!proyecto.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tiene permiso para crear tareas en este proyecto");
        }
        
     // Crear y asignar los valores a la tarea
        Tarea tarea = new Tarea();
        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setPrioridad(tareaDTO.getPrioridad());
        tarea.setActivo(tareaDTO.getActivo());
        tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());
        tarea.setProyecto(proyecto);
        tarea.setUsuario(usuario);

        return tareaRepository.save(tarea);
	}
	

    @Override
    public List<TareaUsuarioDTO> listarTareasPorUsuario(Long usuarioId) {
    	// Obtener el usuario por ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        List<Tarea> tareas = tareaRepository.findByProyectoUsuario(usuario);
        
        return tareas.stream().map(tarea -> {
            TareaUsuarioDTO tareaUsuarioDTO = new TareaUsuarioDTO();
            tareaUsuarioDTO.setDescripcion(tarea.getDescripcion());
            tareaUsuarioDTO.setFechaVencimiento(tarea.getFechaVencimiento());
            tareaUsuarioDTO.setPrioridad(tarea.getPrioridad());
            tareaUsuarioDTO.setActivo(tarea.getActivo());
            tareaUsuarioDTO.setProyectoId(tarea.getProyecto().getId());
            tareaUsuarioDTO.setProyectoNombre(tarea.getProyecto().getNombre()); // Agrega el nombre del proyecto
            return tareaUsuarioDTO;
        }).collect(Collectors.toList());
 
    }

	@Override
	public List<Tarea> listarTareasPorProyecto(Long proyectoId) {
		return tareaRepository.findByProyectoId(proyectoId);
	}


	@Override
	public Tarea actualizarTarea(Long id, TareaDTO tareaDTO, Long usuarioId) {
		
		Tarea tarea = tareaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrado o no tiene permiso para modificarla"));

        validarCambioDeNombre(tarea, tareaDTO, usuarioId);
        
        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setActivo(tareaDTO.getActivo());
        tarea.setPrioridad(tareaDTO.getPrioridad());
        tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());

        return tareaRepository.save(tarea);
	}



	@Override
	public void eliminarTarea(Long id, Long usuarioId) {
		Tarea tarea = tareaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrado o no tiene permiso para eliminarla"));

        tareaRepository.delete(tarea);
		
	}
	


	//VALIDACIONES
		private void validarNombreTareaUnico(String nombreTarea, Long proyectoId) {
		    boolean existeTarea = tareaRepository.existsByDescripcionAndProyectoId(nombreTarea, proyectoId);
		    
		    if (existeTarea) {
		        throw new IllegalArgumentException("Ya existe una tarea con el mismo nombre en este proyecto.");
		    }
		}
	
	private void validarCambioDeNombre(Tarea tarea, TareaDTO tareaDTO, Long usuarioId) {
        if (!tarea.getDescripcion().equals(tareaDTO.getDescripcion())) {
            validarNombreTareaUnico(tareaDTO.getDescripcion(), usuarioId);
        }
    }

	
	@Override
	public List<Tarea> filtrarTareasPorPrioridad(String prioridad, Long usuarioId) {
	    // Buscar las tareas del usuario y filtrar por prioridad
	    return tareaRepository.findByUsuarioIdAndPrioridad(usuarioId, prioridad);
	}

	

	


}
