package com.dacs.backend.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.ServicioRepository;

@Service
public class TurnoServiceImpl implements TurnoService {

    @Autowired
    private CirugiaRepository cirugiaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    LocalTime horaInicio = LocalTime.of(8, 0); // 8:00 AM
    LocalTime horaFin = LocalTime.of(18, 0);   // 6:00 PM
    int tiempoEntreTurnos = 30; 

    @Override
    public List<LocalDateTime> getTurnosDisponibles(Long servicioId, int proximosDias) {
       
        LocalDateTime limite = LocalDateTime.now().plusDays(proximosDias);
        List<Cirugia> turnosOcupados = cirugiaRepository.findByFecha_hora_inicioBefore(limite);
        
        // Obtener duración del servicio en minutos
        Integer duracionServicio = servicioRepository.findById(servicioId)
                .map(s -> s.getDuracionMinutos())
                .orElse(30); // default 30 minutos si no encuentra el servicio
        
        Duration intervaloSlot = Duration.ofMinutes(tiempoEntreTurnos);
        Duration duracion = Duration.ofMinutes(duracionServicio);
        
        // Construir lista de rangos ocupados
        List<Intervalo> ocupados = turnosOcupados.stream()
                .map(c -> new Intervalo(c.getFecha_hora_inicio(), c.getFecha_hora_inicio().plus(intervaloSlot)))
                .collect(Collectors.toList());
        
        List<LocalDateTime> turnosDisponibles = new ArrayList<>();
        
        // Iterar sobre los proximosDias
        LocalDateTime hoy = LocalDateTime.now();
        for (int d = 0; d < proximosDias; d++) {
            LocalDate dia = hoy.toLocalDate().plusDays(d);
            LocalDateTime inicioJornada = LocalDateTime.of(dia, horaInicio);
            LocalDateTime finJornada = LocalDateTime.of(dia, horaFin);
            
            // Generar slots cada tiempoEntreTurnos minutos dentro de la jornada
            for (LocalDateTime slot = inicioJornada; 
                 !slot.plus(duracion).isAfter(finJornada); 
                 slot = slot.plus(intervaloSlot)) {
                
                Intervalo candidato = new Intervalo(slot, slot.plus(duracion));
                
                // Chequear que no se solape con turnos ocupados
                boolean solapo = ocupados.stream()
                        .anyMatch(o -> o.solapaConIntervalo(candidato));
                
                if (!solapo) {
                    turnosDisponibles.add(slot);
                }
            }
        }
        System.err.println("Turnos disponibles calculados: " + turnosDisponibles);

        // Convertir a java.util.Date (si es necesario para la interfaz)
        return turnosDisponibles;
    }
    
    // Helper para chequear solapamientos de intervalos
    private static class Intervalo {
        LocalDateTime inicio;
        LocalDateTime fin;
        
        Intervalo(LocalDateTime inicio, LocalDateTime fin) {
            this.inicio = inicio;
            this.fin = fin;
        }
        
        boolean solapaConIntervalo(Intervalo otro) {
            // Solapan si:
            // - el inicio de uno es antes que el fin del otro
            // - Y el inicio del otro es antes que el fin del uno
            return !this.fin.isBefore(otro.inicio) && !otro.fin.isBefore(this.inicio);
        }
    }
}
