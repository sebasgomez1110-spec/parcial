package com.parcial.saberpro.repository;

import com.parcial.saberpro.entity.ResolucionBeneficio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResolucionBeneficioRepository extends MongoRepository<ResolucionBeneficio, String> {
    List<ResolucionBeneficio> findByVigente(boolean vigente);
    List<ResolucionBeneficio> findByArea(String area);
    List<ResolucionBeneficio> findByVigenteAndArea(boolean vigente, String area);
}