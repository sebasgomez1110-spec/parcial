package com.parcial.saberpro.repository;
import com.parcial.saberpro.entity.Alumno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends MongoRepository<Alumno, String> {
    Optional<Alumno> findFirstByCedulaOrderByFechaCreacionDesc(String cedula); // ← CAMBIADO
    List<Alumno> findByFacultadId(String facultadId);
    List<Alumno> findByEstadoSaberPro(String estado);
    List<Alumno> findByFacultadIdAndEstadoSaberPro(String facultadId, String estado);
    List<Alumno> findByAprobadoPorCoordinacion(boolean aprobado);
    List<Alumno> findByTieneBeneficio(boolean tieneBeneficio);
    long countByFacultadId(String facultadId);
    long countByEstadoSaberPro(String estado);
    boolean existsByCedula(String cedula);
}