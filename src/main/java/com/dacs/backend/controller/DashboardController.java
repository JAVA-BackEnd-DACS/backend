package com.dacs.backend.controller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.EstadisticasGeneralesDto;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.service.CirugiaService;
import com.dacs.backend.service.DashboardService;
import com.dacs.backend.service.PacienteService;
import com.dacs.backend.service.QuirofanoService;

import ch.qos.logback.core.model.Model;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/general")
    public ResponseEntity<EstadisticasGeneralesDto> getGeneralStats() {
        try {
            EstadisticasGeneralesDto stats = dashboardService.obtenerEstadisticasGenerales();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
