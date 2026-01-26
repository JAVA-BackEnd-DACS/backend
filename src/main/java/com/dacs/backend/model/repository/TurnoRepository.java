package com.dacs.backend.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dacs.backend.model.entity.Turno;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    
    List<Turno> findAllByFechaHoraInicioBetween(LocalDateTime start, LocalDateTime end);

    List<Turno> findAllByFechaHoraInicioBetweenAndQuirofanoId(LocalDateTime start, LocalDateTime end, Long quirofanoId);

    List<Turno> findAllByFechaHoraInicioBetweenAndQuirofanoIdAndEstado(LocalDateTime start, LocalDateTime end, Long quirofanoId, String estado);

    List<Turno> findAllByFechaHoraInicioBetweenAndEstado(LocalDateTime start, LocalDateTime end, String estado);

    List<Turno> findAllByCirugiaId(Long cirugiaId);
}
