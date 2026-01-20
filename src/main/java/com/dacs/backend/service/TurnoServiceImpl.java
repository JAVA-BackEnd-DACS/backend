package com.dacs.backend.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.dacs.backend.model.entity.Turno;
import com.dacs.backend.model.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoServiceImpl implements TurnoService {


    @Autowired
    private TurnoRepository turnoRepository;


    @Override
    public List<Turno> getTurnosDisponibles(int pagi, int size, LocalDate fechaInicio, LocalDate fechaFin, Long quirofanoId) {

        List<Turno> turnosDisponibles = new ArrayList<>();

        turnoRepository.findAllByFechaHoraInicioBetween(fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX)).forEach(turno -> {
            if (turno.getEstado().equals("DISPONIBLE") && turno.getQuirofano().getId().equals(quirofanoId)) {
                turnosDisponibles.add(turno);
            }
        });

        return turnosDisponibles;
    }
}
