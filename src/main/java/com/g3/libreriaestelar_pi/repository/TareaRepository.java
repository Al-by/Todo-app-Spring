package com.g3.libreriaestelar_pi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.g3.libreriaestelar_pi.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
	List<Tarea> findByProyectoId(Long proyectoId);

}
