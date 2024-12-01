package com.g3.libreriaestelar_pi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g3.libreriaestelar_pi.dto.TareaDTO;
import com.g3.libreriaestelar_pi.model.Tarea;
import com.g3.libreriaestelar_pi.service.TareaService;

@RestController
@RequestMapping("/api")
public class TareaController {
	
	@Autowired
    private TareaService tareaService;

	//Endpoint para crear una tarea en un proyecto especifico
	@PostMapping("/proyectos/{proyectoId}/tareas") 
    public ResponseEntity<Tarea> crearTarea(@RequestBody TareaDTO tareaDTO, @PathVariable Long proyectoId) {
        Tarea tarea = tareaService.crearTarea(tareaDTO, proyectoId);
        return new ResponseEntity<>(tarea, HttpStatus.CREATED);
    }
	
	//Endpoint para listar todas las tareas
	@GetMapping("/tareas") 
    public ResponseEntity<List<Tarea>> listarTareas() {
        List<Tarea> tareas = tareaService.listarTareas();
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }
	
    @GetMapping("/proyectos/{proyectoId}/tareas")
    public ResponseEntity<List<Tarea>> listarTareasPorProyecto(@PathVariable Long proyectoId) {
        List<Tarea> tareas = tareaService.listarTareasPorProyecto(proyectoId);
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }

    /*// Endpoint para actualizar una tarea existente
    @PutMapping("/{id}/proyecto/{proyectoId}")
    public ResponseEntity<Tarea> actualizarTarea(@PathVariable Long id, @PathVariable Long proyectoId, @RequestBody TareaDTO tareaDTO) {
        Tarea tarea = tareaService.actualizarTarea(id, tareaDTO, proyectoId);
        return new ResponseEntity<>(tarea, HttpStatus.OK);
    }

    // Endpoint para eliminar una tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/

}
