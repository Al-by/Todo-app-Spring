package com.g3.libreriaestelar_pi.serviceImplement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g3.libreriaestelar_pi.dto.TareaDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;
import com.g3.libreriaestelar_pi.model.Tarea;
import com.g3.libreriaestelar_pi.repository.ProyectoRepository;
import com.g3.libreriaestelar_pi.repository.TareaRepository;
import com.g3.libreriaestelar_pi.service.TareaService;

@Service
public class TareaServiceImpl implements TareaService {

	@Autowired
	private TareaRepository tareaRepository;
	
	 @Autowired
	    private ProyectoRepository proyectoRepository;

	@Override
    public Tarea crearTarea(TareaDTO tareaDTO, Long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Tarea tarea = new Tarea();
        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());
        tarea.setPrioridad(tareaDTO.getPrioridad());
        tarea.setProyecto(proyecto);

        return tareaRepository.save(tarea);
    }

	@Override
	public List<Tarea> listarTareas() {

		return tareaRepository.findAll();
	}

	/*
	 @Override
    public List<Tarea> listarTareasPorProyecto(Long proyectoId) {
        return tareaRepository.findByProyectoId(proyectoId);
    }

    @Override
    public Tarea actualizarTarea(Long id, TareaDTO tareaDTO, Long proyectoId) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());
        tarea.setPrioridad(tareaDTO.getPrioridad());
        tarea.setProyecto(proyecto);

        return tareaRepository.save(tarea);
    }

    @Override
    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada");
        }
        tareaRepository.deleteById(id);
    }
	  */


}
