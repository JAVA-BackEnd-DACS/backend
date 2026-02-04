package com.dacs.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.service.CirugiaService;
import com.dacs.backend.service.TurnoService;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.dto.ServicioDto;
import com.dacs.backend.mapper.CirugiaMapper;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.PacienteRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "/cirugia")
public class CirugiaController {

    @Autowired
    private CirugiaService cirugiaService;

    @Autowired
    private TurnoService turnoService;

    @GetMapping("")
    public ResponseEntity<PaginacionDto<CirugiaDTO.Response>> getByPage(
            @RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
            @RequestParam(name = "tamano", required = false, defaultValue = "20") int tamano,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin,
            @RequestParam(name = "estado", required = false) String estado) {
                System.err.println("tamano: " + tamano);
        PaginacionDto<CirugiaDTO.Response> cirugias = cirugiaService.getCirugias(pagina, tamano, parseFecha(fechaInicio), parseFecha(fechaFin), estado);
        return ResponseEntity.ok(cirugias);
    }

    @PostMapping("")
    public ResponseEntity<CirugiaDTO.Response> create(@RequestBody CirugiaDTO.Create cirugiaRequestDto) {
        CirugiaDTO.Response cirugiaResponse = cirugiaService.createCirugia(cirugiaRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cirugiaResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CirugiaDTO.Response> update(@PathVariable Long id,
            @RequestBody CirugiaDTO.Update cirugiaDto) {
        CirugiaDTO.Response response = cirugiaService.updateCirugia(id, cirugiaDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        cirugiaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/equipo-medico")
    public ResponseEntity<List<MiembroEquipoMedicoDto.Response>> getEquipoMedico(@PathVariable Long id) {

        List<MiembroEquipoMedicoDto.Response> equipo = cirugiaService.getEquipoMedico(id);
        return ResponseEntity.ok(equipo);
    }

    @PostMapping("/{id}/equipo-medico")
    public ResponseEntity<List<MiembroEquipoMedicoDto.Response>> postEquipoMedico(@PathVariable Long id,
            @RequestBody List<MiembroEquipoMedicoDto.Create> entityEquipoMedico) {

        List<MiembroEquipoMedicoDto.Response> resp = cirugiaService.saveEquipoMedico(id, entityEquipoMedico);
        return ResponseEntity.status((HttpStatus.CREATED)).body(resp);
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioDto>> getServicios(@RequestParam int pagina, @RequestParam int tamano) {
        return ResponseEntity.ok(cirugiaService.getServicios(pagina, tamano));
    }

    public static LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            return null;
        }
        return LocalDate.parse(fecha); // formato ISO: "yyyy-MM-dd"
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<CirugiaDTO.Response> finalizarCirugia(@PathVariable String id) {
        CirugiaDTO.Response entity = cirugiaService.finalizarCirugia(Long.parseLong(id));        
        return ResponseEntity.ok(entity);
    }
}
