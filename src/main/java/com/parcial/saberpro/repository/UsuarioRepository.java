package com.parcial.saberpro.repository;

import com.parcial.saberpro.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCedula(String cedula);
    List<Usuario> findByRol(String rol);
    List<Usuario> findByRolAndActivo(String rol, boolean activo);
    boolean existsByEmail(String email);
    boolean existsByCedula(String cedula);
}