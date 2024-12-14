package com.g3.libreriaestelar_pi.serviceImplement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.g3.libreriaestelar_pi.repository.ComentarioRepository;
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

	@Autowired
	private ComentarioRepository comentarioRepository;

	@Override
	public Tarea crearTarea(TareaDTO tareaDTO, Long usuarioId) {
		// Validar que el nombre de la tarea sea único dentro del proyecto
		
		validarNombreTareaUnico(tareaDTO.getDescripcion(), tareaDTO.getProyectoId());

		// Validar otros campos
		validarPrioridad(tareaDTO.getPrioridad());
		validarFechaVencimiento(tareaDTO.getFechaVencimiento());
		validarActivo(tareaDTO);

		// Obtener el usuario creador
		Usuario usuarioCreador = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// Obtener el proyecto asociado
		Proyecto proyecto = proyectoRepository.findById(tareaDTO.getProyectoId())
				.orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

		// Validar permisos: el usuario debe ser el propietario o un invitado del proyecto
		if (!proyecto.getOwner().getId().equals(usuarioId) &&
				proyecto.getInvitados().stream().noneMatch(u -> u.getId().equals(usuarioId))) {
			throw new RuntimeException("No tiene permiso para crear tareas en este proyecto");
		}
	    validarDescripcionUnicaEntreInvitados(tareaDTO.getDescripcion(), proyecto.getId(), usuarioId);
		
		boolean esInvitado = proyecto.getInvitados().stream().anyMatch(u -> u.getId().equals(usuarioId));
		
		//Validar que el invitado no repita descripcion de tarea
		validarNombreUnicoParaInvitado(esInvitado, tareaDTO.getDescripcion(), tareaDTO.getProyectoId());
	

		    

		// Crear y asignar los valores a la tarea
		Tarea tarea = new Tarea();
		tarea.setDescripcion(tareaDTO.getDescripcion());
		tarea.setPrioridad(tareaDTO.getPrioridad());
		tarea.setActivo(tareaDTO.getActivo());
		tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());
		tarea.setProyecto(proyecto);
		tarea.setUsuario(usuarioCreador); // Usuario relacionado con la tarea
		tarea.setCreador(usuarioCreador); // Usuario que creó la tarea
		
		
		// Asignar la tarea a un invitado si el usuarioAsignadoId está presente y es un invitado del proyecto
	    if (tareaDTO.getAsignado() != null) {
	        Usuario usuarioAsignado = usuarioRepository.findByEmail(tareaDTO.getAsignado())
	                .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));

	        if (!proyecto.getInvitados().contains(usuarioAsignado) && !proyecto.getOwner().getId().equals(usuarioAsignado.getId())) {
	            throw new RuntimeException("El usuario asignado no es invitado del proyecto o el creador del proyecto");
	        }

	        tarea.setAsignado(usuarioAsignado);
	    }
	    
		// Guardar la tarea
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
		// Buscar la tarea por ID
		Tarea tarea = tareaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

		// Validar permisos: Solo el creador o el dueño del proyecto pueden modificar la tarea
		if (!tarea.getCreador().getId().equals(usuarioId) &&
				!tarea.getProyecto().getOwner().getId().equals(usuarioId)) {
			throw new IllegalArgumentException("No tiene permiso para modificar esta tarea");
		}
		
		
		//Validación de descripción única para invitados
	    boolean esInvitado = tarea.getProyecto().getInvitados().stream()
	            .anyMatch(usuario -> usuario.getId().equals(usuarioId)); // Verificar si el usuario es invitado
	    
	    validarNombreUnicoParaInvitado(esInvitado, tareaDTO.getDescripcion(), tarea.getProyecto().getId());
	    
	 // Validar que no haya otra tarea con la misma descripción en el proyecto
	    boolean existeDescripcionDuplicada = tareaRepository.existsByProyectoIdAndDescripcionAndIdNot(
	            tarea.getProyecto().getId(), tareaDTO.getDescripcion(), tarea.getId());

	    if (existeDescripcionDuplicada) {
	        throw new IllegalArgumentException("Ya existe una tarea con la misma descripción en este proyecto.");
	    }

		// Validaciones de campos
		validarCambioDeNombre(tarea, tareaDTO, usuarioId);
		validarPrioridad(tareaDTO.getPrioridad());
		validarFechaVencimiento(tareaDTO.getFechaVencimiento());
		validarActivo(tareaDTO);

		// Actualizar los valores de la tarea
		tarea.setDescripcion(tareaDTO.getDescripcion());
		tarea.setActivo(tareaDTO.getActivo());
		tarea.setPrioridad(tareaDTO.getPrioridad());
		tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());
		
		// Asignar un usuario si el email  está presente en el DTO
	    if (tareaDTO.getAsignado() != null) {
	        // Obtener el proyecto asociado a la tarea
	        Proyecto proyecto = tarea.getProyecto();

	        // Verificar que el usuario asignado sea un invitado del proyecto o el creador
	        Usuario usuarioAsignado = usuarioRepository.findByEmail(tareaDTO.getAsignado())
	                .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));

	        if (!proyecto.getInvitados().contains(usuarioAsignado) && !proyecto.getOwner().getId().equals(usuarioAsignado.getId())) {
	            throw new IllegalArgumentException("El usuario asignado no es invitado del proyecto o el creador del proyecto");
	        }

	        // Asignar el usuario a la tarea
	        tarea.setAsignado(usuarioAsignado);
	    } else {
	        // Si el valor es null, desasignamos al usuario
	        tarea.setAsignado(null);
	    }
	   

		// Guardar la tarea actualizada
		return tareaRepository.save(tarea);
	}




	@Override
	public void eliminarTarea(Long id, Long usuarioId) {
		// Buscar la tarea por ID
		Tarea tarea = tareaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

		// Validar permisos: Solo el creador o el dueño del proyecto pueden eliminar la tarea
		if (!tarea.getCreador().getId().equals(usuarioId) &&
				!tarea.getProyecto().getOwner().getId().equals(usuarioId)) {
			throw new RuntimeException("No tiene permiso para eliminar esta tarea");
		}

		// Eliminar la tarea (comentarios relacionados se eliminan automáticamente por cascada)
		tareaRepository.delete(tarea);
	}


	

	//VALIDACIONES
	private void validarNombreTareaUnico(String descripcion, Long usuarioId) {
		if (tareaRepository.existsByDescripcionAndUsuarioId(descripcion, usuarioId)) {
	        throw new IllegalArgumentException("Ya existe una tarea con esta descripción para este proyecto.");
	    }
	}
	
	private void validarCambioDeNombre(Tarea tarea, TareaDTO tareaDTO, Long usuarioId) {
        if (!tarea.getDescripcion().equals(tareaDTO.getDescripcion())) {
            validarNombreTareaUnico(tareaDTO.getDescripcion(), usuarioId);
        }
    }
	
	private void validarPrioridad(String prioridad) {
	    List<String> prioridadesPermitidas = Arrays.asList("ALTA", "MEDIA", "BAJA");
	    if (!prioridadesPermitidas.contains(prioridad.toUpperCase())) {
	        throw new IllegalArgumentException("La prioridad debe ser ALTA, MEDIA o BAJA.");
	    }
	}
	
	private void validarFechaVencimiento(LocalDateTime fechaVencimiento) {
	    if (fechaVencimiento.isBefore(LocalDateTime.now())) {
	        throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha actual.");
	    }
	}
	
	private void validarActivo(TareaDTO tarea) {
	    if (tarea.getActivo() == null) {
	        tarea.setActivo(true); // Establece un valor predeterminado si es nulo
	    }
	}
	
	private void validarNombreUnicoParaInvitado(boolean esInvitado, String descripcion, Long proyectoId) {
	    if (esInvitado) {
	        boolean existeTareaConMismoNombre = tareaRepository.existsByDescripcionAndProyectoId(descripcion, proyectoId);

	        if (existeTareaConMismoNombre) {
	            throw new IllegalArgumentException("Ya existe una tarea con la misma descripcion.");
	        }
	    }
	}

	private void validarDescripcionUnicaEntreInvitados(String descripcion, Long proyectoId, Long usuarioId) {
	    // Obtener tareas del proyecto creadas por usuarios invitados
	    List<Tarea> tareasDeInvitados = tareaRepository.findByProyectoIdAndCreadorNot(proyectoId, usuarioId);

	    // Verificar si alguna tarea tiene la misma descripción
	    boolean descripcionDuplicada = tareasDeInvitados.stream()
	            .anyMatch(tarea -> tarea.getDescripcion().equalsIgnoreCase(descripcion));

	    if (descripcionDuplicada) {
	        throw new IllegalArgumentException("No puedes crear o actualizar una tarea con la misma descripción que otra tarea creada por un usuario invitado.");
	    }
	}
	
	
	
	@Override
	public List<Tarea> filtrarTareasPorPrioridad(String prioridad, Long usuarioId) {
	    // Buscar las tareas del usuario y filtrar por prioridad
	    return tareaRepository.findByUsuarioIdAndPrioridad(usuarioId, prioridad);
	}

	

}