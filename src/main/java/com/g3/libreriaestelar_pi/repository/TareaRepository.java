package com.g3.libreriaestelar_pi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.g3.libreriaestelar_pi.model.Tarea;
import com.g3.libreriaestelar_pi.model.Usuario;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
	boolean existsByDescripcionAndProyectoId(String descripcion, Long proyectoId);
	List<Tarea> findByUsuarioId(Long usuarioId);
	List<Tarea> findByProyectoUsuario(Usuario usuario);
	Optional<Tarea> findByIdAndUsuarioId(Long id, Long usuarioId);
	
	List<Tarea> findByUsuarioIdAndPrioridad(Long usuarioId, String prioridad);
    List<Tarea> findByUsuarioIdAndEstado(Long usuarioId, String estado);

}
