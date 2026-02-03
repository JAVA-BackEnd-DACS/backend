package com.dacs.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.TipoIntervencionDto;
import com.dacs.backend.service.TipoIntervencionService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/tipos-intervenciones")
public class TipoIntervencionController {

    @Autowired
    protected TipoIntervencionService tipoIntervencionService;

    @GetMapping("")
    public ResponseEntity<List<TipoIntervencionDto>> getAllTipoIntervencion() {
        List<TipoIntervencionDto> tipoIntervenciones = tipoIntervencionService.getAll();
        return ResponseEntity.ok(tipoIntervenciones);
    }
}
