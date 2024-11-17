package com.g3.libreriaestelar_pi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g3.libreriaestelar_pi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByEmail(String email);
	boolean existsByEmail(String email);
}
