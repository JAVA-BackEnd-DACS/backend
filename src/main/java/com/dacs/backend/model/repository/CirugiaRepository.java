package com.dacs.backend.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dacs.backend.model.entity.Cirugia;

public interface CirugiaRepository extends JpaRepository<Cirugia, Long> {

    Page<Cirugia> findByFecha_hora_inicioBetween(LocalDateTime atStartOfDay, LocalDateTime atTime, Pageable pageable);

    Page<Cirugia> findByFecha_hora_inicioAfter(LocalDateTime atStartOfDay, Pageable pageable);

    Page<Cirugia> findByFecha_hora_inicioBefore(LocalDateTime atTime, Pageable pageable);

}
