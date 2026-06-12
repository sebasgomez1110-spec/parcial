package com.parcial.saberpro.repository;

import com.parcial.saberpro.entity.Facultad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultadRepository extends MongoRepository<Facultad, String> {
    Optional<Facultad> findByCodigo(String codigo);
    List<Facultad> findByActiva(boolean activa);
}