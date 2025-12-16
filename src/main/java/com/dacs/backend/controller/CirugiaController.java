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

import com.dacs.backend.dto.PageResponse;
import com.dacs.backend.dto.ServicioDto;
import com.dacs.backend.mapper.CirugiaMapper;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.PacienteRepository;

import java.time.LocalDateTime;
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

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CirugiaRepository cirugiaRepository; // mover de aca

    @Autowired
    private PacienteRepository pacienteRepository; // mover de aca

    @Autowired
    private CirugiaMapper cirugiaMapper;

    @Autowired
    private com.dacs.backend.model.repository.QuirofanoRepository quirofanoRepository;

    @GetMapping("")
    public PageResponse<CirugiaDTO.Response> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "16") int size) {
        return cirugiaService.get(page, size);
    }
    
    @PostMapping("")
    public CirugiaDTO.Response create(@RequestBody CirugiaDTO.Request cirugiaRequestDto) {
        // delegar al servicio que resuelve relaciones y retorna el DTO de respuesta
        return cirugiaService.create(cirugiaRequestDto);
    }

    @PutMapping("/{id}") //// ????
    public CirugiaDTO.Response update(@PathVariable String id, @RequestBody CirugiaDTO.Request cirugiaDto) {
        Cirugia entity = cirugiaService.getById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Cirugia no encontrada"));

        // Usar mapper para actualizar entidad (mantiene el ID original)
        entity = cirugiaMapper.toEntity(cirugiaDto);

        System.err.println("Datos recibidos para actualizar cirugia: " + cirugiaDto.getQuirofanoId());
        System.err.println("Entidad antes de guardar: " + entity);
        cirugiaService.save(entity);
        System.out.println("Cirugia actualizada: " + entity);
        return cirugiaMapper.toResponseDto(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws Exception {
        java.util.Optional<Cirugia> cirugia = cirugiaService.getById(id);
        if (cirugia.isPresent()) {
            cirugiaService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            throw new Exception("Cirugia no encontrada");
        }
    }

    @GetMapping("/{id}/equipo-medico")
    public ResponseEntity<List<MiembroEquipoMedicoDto.Response>> getEquipoMedico(@PathVariable Long id)
            throws Exception {

        List<MiembroEquipoMedicoDto.Response> EquipoEntity = cirugiaService.getEquipoMedico(id);
        return ResponseEntity.ok(EquipoEntity);
    }

    @PostMapping("/{id}/equipo-medico")
    public ResponseEntity<List<MiembroEquipoMedicoDto.Response>> postEquipoMedico(@PathVariable Long id,
            @RequestBody List<MiembroEquipoMedicoDto.Create> entityEquipoMedico) throws Exception {

        List<MiembroEquipoMedicoDto.Response> resp = cirugiaService.saveEquipoMedico(id, entityEquipoMedico);
        return ResponseEntity.status((HttpStatus.CREATED)).body(resp);
    }

    @GetMapping("/horarios-disponibles")
    public ResponseEntity<List<LocalDateTime>> getTurnosDisponibles(@RequestParam Integer cantidadProximosDias,
            @RequestParam Long servicioId) throws Exception {
        return ResponseEntity.ok(turnoService.getTurnosDisponibles(servicioId, cantidadProximosDias));
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioDto>> getServicios() throws Exception {
        return ResponseEntity.ok(cirugiaService.getServicios());
    }

}
