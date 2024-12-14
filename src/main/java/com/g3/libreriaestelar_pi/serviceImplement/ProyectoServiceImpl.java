package com.g3.libreriaestelar_pi.serviceImplement;

import com.g3.libreriaestelar_pi.dto.ProyectoDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.repository.ProyectoRepository;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import com.g3.libreriaestelar_pi.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProyectoServiceImpl implements ProyectoService {

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Proyecto crearProyecto(ProyectoDTO proyectoDTO, Long usuarioId) {
	    validarNombreNoDuplicadoParaInvitado(proyectoDTO.getNombre(), usuarioId);

		validarNombreUnico(proyectoDTO.getNombre(), usuarioId);

		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		Proyecto proyecto = new Proyecto();
		proyecto.setNombre(proyectoDTO.getNombre());
		proyecto.setDescripcion(proyectoDTO.getDescripcion());
		proyecto.setUsuario(usuario);
		proyecto.setOwner(usuario);

		// Agregar invitados por email
		if (proyectoDTO.getInvitados() != null && !proyectoDTO.getInvitados().isEmpty()) {
			List<Usuario> invitados = usuarioRepository.findByEmailIn(proyectoDTO.getInvitados());

			// Validar que todos los usuarios existen
			if (invitados.size() != proyectoDTO.getInvitados().size()) {
				throw new RuntimeException("Algunos IDs de invitados no son válidos.");
			}
			
			for (Usuario invitado : invitados) {
	            invitado.getProyectosInvitados().add(proyecto);
	        }
	
			proyecto.setInvitados(invitados);
		}

		return proyectoRepository.save(proyecto);
	}

	@Override
	public List<Proyecto> listarProyectosPorUsuario(Long usuarioId) {
		List<Proyecto> proyectosPropios = proyectoRepository.findByUsuarioId(usuarioId);

	    // Obtiene proyectos donde el usuario es invitado
	    List<Proyecto> proyectosInvitado = proyectoRepository.findByInvitadosId(usuarioId);

	    // Combina ambas listas
	    proyectosPropios.addAll(proyectosInvitado);

	    
		return proyectosPropios;
	}

	@Override
	public Proyecto actualizarProyecto(Long id, ProyectoDTO proyectoDTO, Long usuarioId) {
		Proyecto proyecto = proyectoRepository.findByIdAndUsuarioId(id, usuarioId)
				.orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado o no tiene permiso para modificarlo"));

		validarCambioDeNombre(proyecto, proyectoDTO, usuarioId);

		proyecto.setNombre(proyectoDTO.getNombre());
		proyecto.setDescripcion(proyectoDTO.getDescripcion());
		
		
		// Verificar si hay invitados
	    if (proyectoDTO.getInvitados() != null) {
	        // Obtener los usuarios con los ids proporcionados
	        List<Usuario> nuevosInvitados = usuarioRepository.findByEmailIn(proyectoDTO.getInvitados());

	    // Validar que los invitados sean correctos
	    if (nuevosInvitados.size() != proyectoDTO.getInvitados().size()) {
	        throw new IllegalArgumentException("Algunos IDs de invitados no son válidos.");
	    }

	        // Asignar los nuevos invitados al proyecto
	        proyecto.setInvitados(nuevosInvitados);
	    }
	    
	    // Validar que el nombre del proyecto no se repita en proyectos donde el usuario es invitado
	    validarNombreNoDuplicadoParaInvitadoEnActualizacion(proyectoDTO.getNombre(), usuarioId, id);


		return proyectoRepository.save(proyecto);
	}

	@Override
	public void eliminarProyecto(Long id, Long usuarioId) {
		Proyecto proyecto = proyectoRepository.findByIdAndUsuarioId(id, usuarioId)
				.orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado o no tiene permiso para eliminarlo"));

		proyectoRepository.delete(proyecto);
	}

	
	
	
	
	// VALIDACIONES
	private void validarNombreUnico(String nombre, Long usuarioId) {
		boolean existeProyecto = proyectoRepository.existsByNombreAndUsuarioId(nombre, usuarioId);

		if (existeProyecto) {
			throw new IllegalArgumentException("Ya existe un proyecto con este nombre para el usuario.");
		}
	}

	private void validarCambioDeNombre(Proyecto proyecto, ProyectoDTO proyectoDTO, Long usuarioId) {
		if (!proyecto.getNombre().equals(proyectoDTO.getNombre())) {
			validarNombreUnico(proyectoDTO.getNombre(), usuarioId);
		}
	}
	
	private void validarNombreNoDuplicadoParaInvitado(String nombreProyecto, Long usuarioId) {
	    // Buscar proyectos donde el usuario es invitado
	    List<Proyecto> proyectosInvitados = proyectoRepository.findByInvitadosId(usuarioId);

	    // Verificar si alguno de esos proyectos tiene el mismo nombre
	    for (Proyecto proyecto : proyectosInvitados) {
	        if (proyecto.getNombre().equals(nombreProyecto)) {
	            throw new IllegalArgumentException("No puedes crear un proyecto con el mismo nombre al que ya estás invitado.");
	        }
	    }
	}
	
	private void validarNombreNoDuplicadoParaInvitadoEnActualizacion(String nombreProyecto, Long usuarioId, Long proyectoId) {
	    List<Proyecto> proyectosInvitados = proyectoRepository.findByInvitadosId(usuarioId);
	    for (Proyecto proyecto : proyectosInvitados) {
	        
	        if (proyecto.getNombre().equals(nombreProyecto) && !proyecto.getId().equals(proyectoId)) {
	            throw new IllegalArgumentException("No puedes actualizar el proyecto con el mismo nombre al que ya estás invitado.");
	        }
	    }
	}
}
