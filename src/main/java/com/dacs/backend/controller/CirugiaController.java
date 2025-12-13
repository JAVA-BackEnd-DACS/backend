package com.dacs.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.service.CirugiaService;

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
import com.dacs.backend.mapper.CirugiaMapper;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.PacienteRepository;

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
    private ModelMapper modelMapper;

    @Autowired
    private CirugiaRepository cirugiaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CirugiaMapper cirugiaMapper;

    @Autowired
    private com.dacs.backend.model.repository.QuirofanoRepository quirofanoRepository;

    @GetMapping("")
    public PageResponse<CirugiaDTO.Response> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "16") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cirugia> p = cirugiaRepository.findAll(pageable);

        // Map entities -> DTOs (without paciente yet)
        List<Cirugia> entidades = p.getContent();
        List<CirugiaDTO.Response> dtos = entidades.stream()
                .map(e -> modelMapper.map(e, CirugiaDTO.Response.class))
                .collect(Collectors.toList());

        // Recolectar pacienteIds de las cirugías
        List<Long> pacienteIds = entidades.stream()
                .map(Cirugia::getPaciente)
                .filter(Objects::nonNull)
                .map(ent -> ent.getId())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!pacienteIds.isEmpty()) {
            // Cargar pacientes en batch y mapear a DTOs
            List<Paciente> pacientes = pacienteRepository.findAllById(pacienteIds);
            Map<Long, Paciente> pacientesMap = pacientes.stream()
                    .collect(Collectors.toMap(Paciente::getId, pc -> pc));

            for (int i = 0; i < entidades.size(); i++) {
                var entidad = entidades.get(i);
                var dto = dtos.get(i);
                if (entidad.getPaciente() != null && entidad.getPaciente().getId() != null) {
                    var pacienteEntity = pacientesMap.get(entidad.getPaciente().getId());
                    if (pacienteEntity != null) {
                        var pacienteDto = modelMapper.map(pacienteEntity, PacienteDTO.Response.class);
                        dto.setPaciente(pacienteDto); // requiere que CirugiaDTO tenga setPaciente(PacienteDto)
                    }
                }
            }
        }

        PageResponse<CirugiaDTO.Response> resp = new PageResponse<>();
        resp.setContent(dtos);
        resp.setNumber(p.getNumber());
        resp.setSize(p.getSize());
        resp.setTotalElements(p.getTotalElements());
        resp.setTotalPages(p.getTotalPages());
        return resp;
    }

    @PostMapping("")
    public CirugiaDTO.Response create(@RequestBody CirugiaDTO.Create cirugiaRequestDto) {
        // delegar al servicio que resuelve relaciones y retorna el DTO de respuesta
        return cirugiaService.create(cirugiaRequestDto);
    }

    @PutMapping("/{id}")
    public CirugiaDTO.Response update(@PathVariable String id, @RequestBody CirugiaDTO.Update cirugiaDto) {
        Cirugia entity = cirugiaService.getById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Cirugia no encontrada"));
        
        // Mapear campos simples manualmente para evitar problemas con relaciones
        entity.setServicio(cirugiaDto.getServicio());
        entity.setPrioridad(cirugiaDto.getPrioridad());
        entity.setFecha_hora_inicio(cirugiaDto.getFecha_hora_inicio());
        entity.setEstado(cirugiaDto.getEstado());
        entity.setAnestesia(cirugiaDto.getAnestesia());
        entity.setTipo(cirugiaDto.getTipo());
        
        // Mapear relaciones (paciente y quirofano) si vienen en el DTO
        if (cirugiaDto.getPaciente() != null) {
            Paciente paciente = pacienteRepository.findById(cirugiaDto.getPaciente())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            entity.setPaciente(paciente);
        }
        
        if (cirugiaDto.getQuirofano() != null) {
            Quirofano quirofano = quirofanoRepository.findById(cirugiaDto.getQuirofano())
                    .orElseThrow(() -> new RuntimeException("Quirofano no encontrado"));
            entity.setQuirofano(quirofano);
        }
        
        System.err.println("Datos recibidos para actualizar cirugia: " + cirugiaDto.getQuirofano());
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
    public ResponseEntity<List<MiembroEquipoMedicoDto.Response>> getEquipoMedico(@PathVariable Long id) {

        List<MiembroEquipoMedicoDto.Response> EquipoEntity = cirugiaService.getEquipoMedico(id);
        return ResponseEntity.ok(EquipoEntity);
    }

    @PostMapping("/{id}/equipo-medico")
    public ResponseEntity<List<MiembroEquipoMedicoDto.Response>> postEquipoMedico(@PathVariable Long id,
            @RequestBody List<MiembroEquipoMedicoDto.Create> entityEquipoMedico) {

        List<MiembroEquipoMedicoDto.Response> resp = cirugiaService.saveEquipoMedico(id, entityEquipoMedico);
        return ResponseEntity.status((HttpStatus.CREATED)).body(resp);
    }

}
