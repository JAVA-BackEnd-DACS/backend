package com.dacs.backend.controller;

import com.dacs.backend.dto.IntervencionDto;
import com.dacs.backend.service.IntervencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("")
public class IntervencionController {

    @Autowired
    private IntervencionService intervencionService;

    @GetMapping("/cirugia/{cirugiaId}/intervenciones")
    public ResponseEntity<List<IntervencionDto>> getIntervencionesByCirugiaId(@PathVariable("cirugiaId") Long cirugiaId) {
        List<IntervencionDto> intervenciones = intervencionService.getByCirugiaId(cirugiaId);
        System.out.println("Intervenciones for cirugiaId " + cirugiaId + ": " + intervenciones);
        return ResponseEntity.ok(intervenciones);
    }

    @PostMapping("/cirugia/{cirugiaId}/intervenciones")
    public ResponseEntity<IntervencionDto> createIntervencion(@PathVariable("cirugiaId") Long cirugiaId,
                                                             @RequestBody IntervencionDto intervencion) {
        IntervencionDto created = intervencionService.createIntervencion(cirugiaId, intervencion);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/cirugia/{cirugiaId}/intervenciones/{intervencionId}")
    public ResponseEntity<IntervencionDto> updateIntervencion(@PathVariable("cirugiaId") Long cirugiaId,
                                                             @PathVariable("intervencionId") Long intervencionId,
                                                             @RequestBody IntervencionDto intervencion) {
        IntervencionDto updated = intervencionService.updateIntervencion(cirugiaId, intervencionId, intervencion);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cirugia/{cirugiaId}/intervenciones/{intervencionId}")
    public ResponseEntity<Void> deleteIntervencion(@PathVariable("cirugiaId") Long cirugiaId,
                                                  @PathVariable("intervencionId") Long intervencionId) {
        intervencionService.deleteIntervencion(cirugiaId, intervencionId);
        return ResponseEntity.noContent().build();
    }
}
