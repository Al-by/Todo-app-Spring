package com.g3.libreriaestelar_pi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g3.libreriaestelar_pi.controller.ProyectoController.ErrorResponse;
import com.g3.libreriaestelar_pi.dto.ProyectoDTO;
import com.g3.libreriaestelar_pi.dto.TareaDTO;
import com.g3.libreriaestelar_pi.dto.TareaUsuarioDTO;
import com.g3.libreriaestelar_pi.model.Proyecto;
import com.g3.libreriaestelar_pi.model.Tarea;
import com.g3.libreriaestelar_pi.model.Usuario;
import com.g3.libreriaestelar_pi.repository.ProyectoRepository;
import com.g3.libreriaestelar_pi.repository.UsuarioRepository;
import com.g3.libreriaestelar_pi.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
	
	@Autowired
    private TareaService tareaService;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	private Usuario obtenerUsuarioLogueado() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping("/crear")
    public ResponseEntity<Object> crearTarea(@Valid @RequestBody TareaDTO tareaDTO) {
        Usuario usuario = obtenerUsuarioLogueado();
        try {
            Tarea tarea = tareaService.crearTarea(tareaDTO, usuario.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(tarea);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/proyectos/{proyectoId}")
    public ResponseEntity<List<Tarea>> listarTareasPorProyecto(@PathVariable Long proyectoId) {
        List<Tarea> tareas = tareaService.listarTareasPorProyecto(proyectoId);
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    }
   
    @GetMapping("/listar")
    public ResponseEntity<Object> listarTareasPorUsuario() {
        try {
        	Usuario usuario = obtenerUsuarioLogueado();
            
            // Listar las tareas del usuario
            List<TareaUsuarioDTO> tareas = tareaService.listarTareasPorUsuario(usuario.getId());
            
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            // Manejar errores y devolver una respuesta adecuada
            ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "Ocurrió un error inesperado.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Tarea> actualizarTarea(@PathVariable Long id, @RequestBody TareaDTO tareaDTO) {
        Usuario usuario = obtenerUsuarioLogueado();
        Tarea tareaActualizada = tareaService.actualizarTarea(id, tareaDTO, usuario.getId());
        return ResponseEntity.ok(tareaActualizada);
    }
    
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioLogueado();
        tareaService.eliminarTarea(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }
    
    
    
    @GetMapping("/filtrar/por-prioridad")
    public ResponseEntity<List<Tarea>> filtrarTareasPorPrioridad(@RequestParam("prioridad") String prioridad) {
        Usuario usuario = obtenerUsuarioLogueado();
        List<Tarea> tareas = tareaService.filtrarTareasPorPrioridad(prioridad, usuario.getId());
        return ResponseEntity.ok(tareas);
    }

    
    public class ErrorResponse {
        private String errorCode;
        private String message;

        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        // Getters y setters
        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    
    
  
 }


    
    

	



