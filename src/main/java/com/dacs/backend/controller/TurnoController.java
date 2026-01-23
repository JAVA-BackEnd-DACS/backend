package com.dacs.backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.model.entity.Turno;
import com.dacs.backend.service.TurnoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    //@Autowired
    //private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<Turno>> getTurnosDisponibles(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long quirofanoId) {
        return ResponseEntity
                .ok(turnoService.getTurnosDisponibles(page, size, parseFecha(fechaInicio), parseFecha(fechaFin), quirofanoId));
    }

    @GetMapping("/disponible")
    public ResponseEntity<Boolean> EstaDisponible(@RequestParam String fechaInicio,
        @RequestParam String fechaFin,
        @RequestParam Long quirofanoId) {    

        return ResponseEntity.ok(
            turnoService.verificarDisponibilidadTurno(
                quirofanoId, 
                parseFecha(fechaInicio), 
                parseFecha(fechaFin)
            )
        );
    }
    
    public static LocalDate parseFecha(String fecha) {
        return LocalDate.parse(fecha); // formato ISO: "yyyy-MM-dd"
    }


}
