package com.g3.libreriaestelar_pi.repository;

import com.g3.libreriaestelar_pi.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
	boolean existsByNombreAndUsuarioId(String nombre, Long usuarioId);
    List<Proyecto> findByUsuarioId(Long usuarioId);
    List<Proyecto> findByInvitadosId(@Param("usuarioId") Long usuarioId);
    Optional<Proyecto> findByIdAndUsuarioId(Long id, Long usuarioId);
    boolean existsByNombreAndOwnerId(String nombre, Long ownerId);
}
