package com.dacs.backend.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.dacs.backend.model.entity.Turno;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.model.repository.TurnoRepository;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.QuirofanoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoServiceImpl implements TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private CirugiaRepository cirugiaRepository;
    @Autowired
    private QuirofanoRepository quirofanoRepository;

    @Override
    public List<Turno> getTurnosDisponibles(int pagi, int size, LocalDateTime fechaInicio, LocalDateTime fechaFin,
            Long quirofanoId) {
        List<Turno> turnosDisponibles = new ArrayList<>();

        turnoRepository.findAllByFechaHoraInicioBetween(fechaInicio, fechaFin)
                .forEach(turno -> {
                    if (turno.getEstado().equals("DISPONIBLE") && turno.getQuirofano().getId().equals(quirofanoId)) {
                        turnosDisponibles.add(turno);
                    }
                });

        return turnosDisponibles;
    }

    @Override
    public Boolean verificarDisponibilidadTurno(Long quirofanoId, LocalDateTime fechaHoraInicio,
            LocalDateTime fechaHoraFin) {
        long minutos = java.time.Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes();
        long cantidad = minutos / 30;
        System.out.println("Cantidad de turnos necesarios: " + cantidad);
        System.out.println("FechaHoraIniciod: " + fechaHoraInicio);
        System.out.println("FechaHoraFin: " + fechaHoraFin);
        return turnoRepository.findAllByFechaHoraInicioBetweenAndQuirofanoIdAndEstado(
                fechaHoraInicio, fechaHoraFin, quirofanoId, "DISPONIBLE").size() > cantidad;
    }

    @Override
    public Turno asignarTurno(Long cirugiaId, Long quirofanoId, LocalDateTime fechaHoraInicio,
            LocalDateTime fechaHoraFin) {
        List<Turno> turnos = turnoRepository.findAllByFechaHoraInicioBetweenAndQuirofanoIdAndEstado(fechaHoraInicio,
            fechaHoraFin, quirofanoId, "DISPONIBLE");
        System.err.println("Turnos disponibles encontrados: " + turnos);  
        if (turnos == null || turnos.isEmpty()) {
            throw new IllegalArgumentException(
                "No hay turnos disponibles para el quirófano en la fecha y hora solicitadas.");
        }
        Cirugia cirugia = cirugiaRepository.findById(cirugiaId)
            .orElseThrow(() -> new IllegalArgumentException("Cirugía no encontrada con ID: " + cirugiaId));

        for (Turno t : turnos) {
            t.setCirugia(cirugia);
            t.setEstado("ASIGNADO");
        }
        turnoRepository.saveAll(turnos);
        return turnos.get(0);
    }

    @Override
    public void borrarTurno(Long cirugiaId) {
        List<Turno> turnos = turnoRepository.findAllByCirugiaId(cirugiaId);
        for (Turno turno : turnos) {
            turno.setEstado("DISPONIBLE");
            turno.setCirugia(null);
            turnoRepository.save(turno);
        }
    }

    // Método para generar los turnos
    @Override
    public void generarTurnosAutomaticaMensual() {
        // Borrar todos los turnos existentes
        turnoRepository.deleteAll();

        // Obtención de los quirófanos disponibles
        List<Quirofano> quirofanos = quirofanoRepository.findAll();

        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaLimite = ahora.plusDays(30);

        // Generar turnos durante los próximos 30 días
        for (LocalDateTime fecha = ahora.truncatedTo(ChronoUnit.DAYS); fecha.isBefore(fechaLimite); fecha = fecha.plusDays(1)) {
            LocalDateTime horaInicio = fecha.withHour(8).withMinute(0).withSecond(0).withNano(0); // 8:00 AM
            LocalDateTime horaFin = fecha.withHour(18).withMinute(0).withSecond(0).withNano(0); // 6:00 PM

            while (horaInicio.isBefore(horaFin)) {
                for (Quirofano quirofano : quirofanos) {
                    Turno turno = new Turno();
                    turno.setFechaHoraInicio(horaInicio);
                    turno.setQuirofano(quirofano);
                    turno.setEstado("DISPONIBLE");
                    turno.setCirugia(null);
                    turnoRepository.save(turno);
                }
                horaInicio = horaInicio.plusMinutes(30);
            }
        }
    }
}
