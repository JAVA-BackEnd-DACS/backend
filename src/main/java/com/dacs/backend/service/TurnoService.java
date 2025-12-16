package com.dacs.backend.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.dacs.backend.model.entity.Cirugia;

public interface TurnoService  {

    List<LocalDateTime> getTurnosDisponibles(Long servicioId, int proximosDias);
}
