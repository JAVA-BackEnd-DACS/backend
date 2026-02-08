package com.dacs.backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.dto.TurnoDTO;
import com.dacs.backend.model.entity.Turno;
import com.dacs.backend.service.TurnoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    // @Autowired
    // private ModelMapper modelMapper;

    @GetMapping("")
    public PaginacionDto.Response<TurnoDTO> getTurnosDisponibles(
            @RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
            @RequestParam(name = "tamano", required = false, defaultValue = "300") int tamano,
            @RequestParam(required = true) String fechaInicio,
            @RequestParam(required = true) String fechaFin,
            @RequestParam(required = false, defaultValue = "0") int quirofanoId,
            @RequestParam(required = false, defaultValue = "") String estado) {
        System.out.println("asdasdas:" + quirofanoId);
        return turnoService.getTurnosDisponibles(pagina, tamano, parseFecha(fechaInicio), parseFecha(fechaFin),
                quirofanoId, estado);
    }

    @GetMapping("/disponible")
    public ResponseEntity<Boolean> EstaDisponible(@RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long quirofanoId) {

        return ResponseEntity.ok(
                turnoService.verificarDisponibilidadTurno(
                        quirofanoId,
                        parseFecha(fechaInicio),
                        parseFecha(fechaFin)));
    }

    @PostMapping("/generar-turnos")
    public ResponseEntity<Void> generarTurnos() {
        turnoService.generarTurnosAutomaticaMensual();
        return ResponseEntity.ok().build();
    }

    public static LocalDateTime parseFecha(String fecha) {
        return LocalDateTime.parse(fecha); // formato ISO: "yyyy-MM-dd"
    }

}
