package com.dacs.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.dacs.backend.model.entity.Turno;


public interface TurnoService  {

    List<Turno> getTurnosDisponibles(int pagi, int size, LocalDate fechaInicio, LocalDate fechaFin, Long quirofanoId);
}
