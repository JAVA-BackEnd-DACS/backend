package com.dacs.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.dto.TurnoDTO;
import com.dacs.backend.model.entity.Turno;


public interface TurnoService  {

    PaginacionDto.Response<TurnoDTO> getTurnosDisponibles(int pagina, int tamano, LocalDateTime fechaInicio, LocalDateTime fechaFin, int quirofanoId, String estado);

    Boolean verificarDisponibilidadTurno(Long quirofanoId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin);

    Turno asignarTurno(Long cirugiaId, Long quirofanoId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin);

    void borrarTurno(Long cirugiaId);

    void generarTurnosAutomaticaMensual();
}
