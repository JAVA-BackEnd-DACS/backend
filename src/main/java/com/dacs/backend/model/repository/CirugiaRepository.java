package com.dacs.backend.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dacs.backend.model.entity.Cirugia;

public interface CirugiaRepository extends JpaRepository<Cirugia, Long> {

    Page<Cirugia> findByFechaHoraInicioBetween(LocalDateTime atStartOfDay, LocalDateTime atTime, Pageable pageable);

    Page<Cirugia> findByFechaHoraInicioAfter(LocalDateTime atStartOfDay, Pageable pageable);

    Page<Cirugia> findByFechaHoraInicioBefore(LocalDateTime atTime, Pageable pageable);

}
