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
    	validarNombreUnico(proyectoDTO.getNombre(), usuarioId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(proyectoDTO.getNombre());
        proyecto.setDescripcion(proyectoDTO.getDescripcion());
        proyecto.setUsuario(usuario);

        return proyectoRepository.save(proyecto);
    }

    @Override
    public List<Proyecto> listarProyectosPorUsuario(Long usuarioId) {
        return proyectoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Proyecto actualizarProyecto(Long id, ProyectoDTO proyectoDTO, Long usuarioId) {
        Proyecto proyecto = proyectoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no tiene permiso para modificarlo"));

        validarCambioDeNombre(proyecto, proyectoDTO, usuarioId);

        proyecto.setNombre(proyectoDTO.getNombre());
        proyecto.setDescripcion(proyectoDTO.getDescripcion());

        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminarProyecto(Long id, Long usuarioId) {
        Proyecto proyecto = proyectoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no tiene permiso para eliminarlo"));

        proyectoRepository.delete(proyecto);
    }
    
    //VALIDACIONES
    private void validarNombreUnico(String nombre, Long usuarioId) {
        if (proyectoRepository.existsByNombreAndUsuarioId(nombre, usuarioId)) {
            throw new RuntimeException("Ya existe un proyecto con este nombre para el usuario.");
        }
    }
    
    private void validarCambioDeNombre(Proyecto proyecto, ProyectoDTO proyectoDTO, Long usuarioId) {
        if (!proyecto.getNombre().equals(proyectoDTO.getNombre())) {
            validarNombreUnico(proyectoDTO.getNombre(), usuarioId);
        }
    }
}
