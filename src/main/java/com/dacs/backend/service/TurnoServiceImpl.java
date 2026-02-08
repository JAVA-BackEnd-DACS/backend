package com.dacs.backend.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.dacs.backend.model.entity.Turno;
import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.dto.TurnoDTO;
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
    public PaginacionDto.Response<TurnoDTO> getTurnosDisponibles(int pagina, int tamano, LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            int quirofanoId, String estado) {
        // Si fechaInicio o fechaFin llegan como string vacío, asignar valores por defecto
        if (fechaInicio == null) {
            fechaInicio = LocalDateTime.now();
        }
        if (fechaFin == null) {
            fechaFin = fechaInicio.plusDays(30);
        }


        // --- INICIO CAMBIO: Filtrar turnos realmente disponibles para la duración ---
        // Suponiendo que la duración del servicio se recibe como parámetro adicional (ej: minutosDuracion)
        int minutosDuracion = 60; // <-- AJUSTAR: obtener este valor según tu lógica/endpoint
        int bloquesNecesarios = minutosDuracion / 30;
        if (minutosDuracion % 30 != 0) bloquesNecesarios++;

        List<Turno> turnos;
        boolean filtrarQuirofano = (quirofanoId != 0);
        if (filtrarQuirofano) {
            turnos = turnoRepository.findAllByFechaHoraInicioBetweenAndQuirofanoId(
                fechaInicio, fechaFin, (long) quirofanoId);
        } else {
            turnos = turnoRepository.findAllByFechaHoraInicioBetween(fechaInicio, fechaFin);
        }

        // Solo considerar turnos DISPONIBLE cuyo bloque y los siguientes estén libres
        List<com.dacs.backend.dto.TurnoDTO> turnoDTOs = new ArrayList<>();
        for (Turno t : turnos) {
            if (!"DISPONIBLE".equalsIgnoreCase(t.getEstado())) continue;
            boolean disponible = true;
            LocalDateTime actual = t.getFechaHoraInicio();
            for (int i = 0; i < bloquesNecesarios; i++) {
                LocalDateTime bloque = actual.plusMinutes(i * 30);
                boolean bloqueLibre = turnos.stream().anyMatch(tt ->
                    tt.getFechaHoraInicio().equals(bloque) && "DISPONIBLE".equalsIgnoreCase(tt.getEstado())
                );
                if (!bloqueLibre) {
                    disponible = false;
                    break;
                }
            }
            if (disponible) {
                com.dacs.backend.dto.TurnoDTO dto = new com.dacs.backend.dto.TurnoDTO();
                dto.setId(t.getId());
                dto.setFechaHoraInicio(t.getFechaHoraInicio());
                dto.setEstado(t.getEstado());
                if (t.getQuirofano() != null) {
                    dto.setQuirofanoId(t.getQuirofano().getId());
                }
                if (t.getCirugia() != null) {
                    dto.setCirugiaId(t.getCirugia().getId());
                }
                turnoDTOs.add(dto);
            }
        }
        // --- FIN CAMBIO ---

        // Pagination logic
        int totalElementos = turnoDTOs.size();
        int fromIndex = Math.min(pagina * tamano, totalElementos);
        int toIndex = Math.min(fromIndex + tamano, totalElementos);
        List<com.dacs.backend.dto.TurnoDTO> pageContent = turnoDTOs.subList(fromIndex, toIndex);

        PaginacionDto.Response<com.dacs.backend.dto.TurnoDTO> resp = new PaginacionDto.Response<>();
        resp.setContenido(pageContent);
        resp.setPagina(pagina);
        resp.setTamaño(tamano);
        resp.setTotalElementos(totalElementos);
        resp.setTotalPaginas((int) Math.ceil((double) totalElementos / tamano));
        return resp;
    }

    @Override
    public Boolean verificarDisponibilidadTurno(Long quirofanoId, LocalDateTime fechaHoraInicio,
            LocalDateTime fechaHoraFin) {
        long minutos = java.time.Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes();
        long cantidad = minutos / 30;
        System.out.println("Cantidad de turnos necesarios: " + cantidad);
        System.out.println("FechaHoraIniciod: " + fechaHoraInicio);
        System.out.println("FechaHoraFin: " + fechaHoraFin);

        // Obtener todos los turnos en el rango para el quirófano
        List<Turno> turnos = turnoRepository.findAllByFechaHoraInicioBetweenAndQuirofanoId(
            fechaHoraInicio, fechaHoraFin.minusMinutes(1), quirofanoId);

        // Verificar que todos los bloques requeridos estén DISPONIBLE y sean consecutivos
        for (int i = 0; i < cantidad; i++) {
            LocalDateTime actual = fechaHoraInicio.plusMinutes(i * 30);
            boolean disponible = turnos.stream().anyMatch(t ->
                t.getFechaHoraInicio().equals(actual) &&
                "DISPONIBLE".equalsIgnoreCase(t.getEstado())
            );
            if (!disponible) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Turno asignarTurno(Long cirugiaId, Long quirofanoId, LocalDateTime fechaHoraInicio,
            LocalDateTime fechaHoraFin) {
        List<Turno> turnos = turnoRepository.findAllByFechaHoraInicioBetweenAndQuirofanoIdAndEstado(fechaHoraInicio,
                fechaHoraFin, quirofanoId, "DISPONIBLE");
        // Filtrar solo los turnos cuyo fechaHoraInicio sea >= fechaHoraInicio y <
        // fechaHoraFin
        List<Turno> turnosEnRango = new ArrayList<>();
        for (Turno t : turnos) {
            if (!t.getFechaHoraInicio().isBefore(fechaHoraInicio) && t.getFechaHoraInicio().isBefore(fechaHoraFin)) {
                turnosEnRango.add(t);
            }
        }
        System.err.println("Turnos disponibles encontrados: " + turnosEnRango);
        if (turnosEnRango.isEmpty()) {
            throw new IllegalArgumentException(
                    "No hay turnos disponibles para el quirófano en la fecha y hora solicitadas.");
        }
        Cirugia cirugia = cirugiaRepository.findById(cirugiaId)
                .orElseThrow(() -> new IllegalArgumentException("Cirugía no encontrada con ID: " + cirugiaId));

        for (Turno t : turnosEnRango) {
            t.setCirugia(cirugia);
            t.setEstado("ASIGNADO");
        }
        turnoRepository.saveAll(turnosEnRango);
        return turnosEnRango.get(0);
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
        for (LocalDateTime fecha = ahora.truncatedTo(ChronoUnit.DAYS); fecha
                .isBefore(fechaLimite); fecha = fecha.plusDays(1)) {
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
