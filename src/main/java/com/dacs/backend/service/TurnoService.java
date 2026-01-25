package com.dacs.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.dacs.backend.model.entity.Turno;


public interface TurnoService  {

    List<Turno> getTurnosDisponibles(int pagi, int size, LocalDateTime fechaInicio, LocalDateTime fechaFin, Long quirofanoId);

    Boolean verificarDisponibilidadTurno(Long quirofanoId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin);

    Turno asignarTurno(Long cirugiaId, Long quirofanoId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin);

    void borrarTurno(Long cirugiaId);

    void generarTurnosAutomaticaMensual();
}
