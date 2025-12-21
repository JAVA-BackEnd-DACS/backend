package com.dacs.backend.service;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.dto.PageResponse;
import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.repository.PacienteRepository;

import io.micrometer.common.lang.NonNull;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Optional<Paciente> getById(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente save(Paciente entity) {
        return pacienteRepository.save(entity);
    }

    @Override
    public Boolean existById(Long id) {
        return pacienteRepository.existsById(id);
    }

    @Override
    public List<Paciente> getAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Optional<Paciente> paciente = getById(id);
        pacienteRepository.delete(paciente.get());
    }

    @Override
    public Paciente getBy(Map<String, Object> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Paciente> getByDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    @Override
    public PacienteDTO.Response crear(PacienteDTO.Request pacienteDTO) {
        Paciente paciente = modelMapper.map(pacienteDTO, Paciente.class);
        Paciente savedPaciente = pacienteRepository.save(paciente);
        return modelMapper.map(savedPaciente, PacienteDTO.Response.class);
    }

    @Override
    public List<PacienteDTO.Response> getPacientesByIds(List<Long> ids) {
        List<Paciente> pacientes = pacienteRepository.findAllById(ids);
        List<PacienteDTO.Response> pacientesDTO = pacientes.stream().map(p -> {
            PacienteDTO.Response dto = new PacienteDTO.Response();
            dto.setId(p.getId());
            dto.setNombre(p.getNombre());
            dto.setDni(p.getDni());
            dto.setFecha_nacimiento((p.getFecha_nacimiento()));
            dto.setTelefono(p.getTelefono());
            dto.setPeso(p.getPeso());
            dto.setDireccion(p.getDireccion());
            return dto;
        }).toList();
        return pacientesDTO;
    }

    @Override
    public PageResponse<PacienteDTO.Response> getByPage(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Paciente> pacientePage;

        // Si hay search, filtrar; si no, traer todos
        if (search != null && !search.isBlank()) {
            pacientePage = pacienteRepository.findBySearch(search, pageable);
        } else {
            pacientePage = pacienteRepository.findAll(pageable);
        }

        // Mapear a DTO
        List<PacienteDTO.Response> content = pacientePage.getContent().stream()
                .map(p -> modelMapper.map(p, PacienteDTO.Response.class))
                .toList();

        // Retornar PageResponse con metadata de paginación
        PageResponse<PacienteDTO.Response> response = new PageResponse<>();
        response.setContent(content);
        response.setTotalElements(pacientePage.getTotalElements());
        response.setTotalPages(pacientePage.getTotalPages());
        response.setNumber(page);
        response.setSize(size);
        return response;
    }
}
