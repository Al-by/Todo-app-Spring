package com.g3.libreriaestelar_pi.repository;

import com.g3.libreriaestelar_pi.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByTareaId(Long tareaId);
}
