package com.dacs.backend.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.dto.QuirofanoDTO;
import com.dacs.backend.dto.ServicioDto;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.model.entity.Servicio;
import com.dacs.backend.model.repository.PacienteRepository;
import com.dacs.backend.model.repository.QuirofanoRepository;
import com.dacs.backend.model.repository.ServicioRepository;

@Component
public class CirugiaMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private QuirofanoRepository quirofanoRepository;

    public Cirugia toEntity(CirugiaDTO.Create dto) {
        Cirugia entity = new Cirugia();
        entity.setPrioridad(dto.getPrioridad());
        entity.setFecha_hora_inicio(dto.getFecha_hora_inicio());
        entity.setEstado(dto.getEstado());
        entity.setAnestesia(dto.getAnestesia());
        entity.setTipo(dto.getTipo());

        // resolver relaciones por id (si vienen)
        if (dto.getServicioId() != null) {
            Servicio s = servicioRepository.findById(dto.getServicioId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Servicio no encontrado id=" + dto.getServicioId()));
            entity.setServicio(s);
        }
        entity.setTipo(dto.getTipo());

        // resolver relaciones por id (si vienen)
        if (dto.getPacienteId() != null) {
            Paciente p = pacienteRepository.findById(dto.getPacienteId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Paciente no encontrado id=" + dto.getPacienteId()));
            entity.setPaciente(p);
        }
        if (dto.getQuirofanoId() != null) {
            Quirofano q = quirofanoRepository.findById(dto.getQuirofanoId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Quirofano no encontrado id=" + dto.getQuirofanoId()));
            entity.setQuirofano(q);
        }
        return entity;
    }

    public CirugiaDTO.Response toResponseDto(Cirugia entity) {
        CirugiaDTO.Response dto = modelMapper.map(entity, CirugiaDTO.Response.class);
        if (entity.getPaciente() != null) {
            PacienteDTO.Response pDto = modelMapper.map(entity.getPaciente(), PacienteDTO.Response.class);
            dto.setPaciente(pDto);
        }
        if (entity.getQuirofano() != null) {
            QuirofanoDTO qDto = modelMapper.map(entity.getQuirofano(), QuirofanoDTO.class);
            dto.setQuirofano(qDto);
        }
        if (entity.getServicio() != null) {
            ServicioDto sDto = modelMapper.map(entity.getServicio(), ServicioDto.class);
            dto.setServicio(sDto);
        }
        return dto;
    }
}