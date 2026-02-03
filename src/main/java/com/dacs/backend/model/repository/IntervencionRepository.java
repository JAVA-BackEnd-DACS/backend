package com.dacs.backend.model.repository;

import com.dacs.backend.model.entity.Intervencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervencionRepository extends JpaRepository<Intervencion, Long> {
    // TODO: Add custom query for cirugiaId if needed
}
