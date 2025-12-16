package com.dacs.backend.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dacs.backend.model.entity.Cirugia;

public interface CirugiaRepository extends JpaRepository<Cirugia, Long> {

    // Buscar cirugías por rango de fecha/hora (inicio inclusivo)
    @Query("SELECT c FROM Cirugia c WHERE c.fecha_hora_inicio BETWEEN :start AND :end")
    List<Cirugia> findByFecha_hora_inicioBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Buscar cirugías antes de cierta fecha/hora
    @Query("SELECT c FROM Cirugia c WHERE c.fecha_hora_inicio < :before")
    List<Cirugia> findByFecha_hora_inicioBefore(@Param("before") LocalDateTime before);
}
