package com.dacs.backend.service;

import com.dacs.backend.dto.IntervencionDto;
import com.dacs.backend.model.entity.Intervencion;
import com.dacs.backend.model.repository.IntervencionRepository;
import com.dacs.backend.model.repository.CirugiaRepository;
import com.dacs.backend.model.repository.TipoIntervencionRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntervencionServiceImpl implements IntervencionService {


    @Autowired
    private IntervencionRepository intervencionRepository;

    @Autowired
    private CirugiaRepository cirugiaRepository;

    @Autowired
    private TipoIntervencionRepository tipoIntervencionRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<IntervencionDto> getByCirugiaId(Long cirugiaId) {
        return intervencionRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public IntervencionDto createIntervencion(Long cirugiaId, IntervencionDto intervencion) {
        Intervencion entity = new Intervencion();
        entity.setObservaciones(intervencion.getObservaciones());
        // Set cirugia reference
        entity.setCirugia(cirugiaRepository.findById(cirugiaId).orElseThrow(() -> new RuntimeException("Cirugia not found")));
        // Set tipoIntervencion reference
        entity.setTipoIntervencion(tipoIntervencionRepository.findById(intervencion.getTipoIntervencionId()).orElseThrow(() -> new RuntimeException("TipoIntervencion not found")));
        Intervencion saved = intervencionRepository.save(entity);
        return modelMapper.map(saved, IntervencionDto.class);
    }

    @Override
    public IntervencionDto updateIntervencion(Long cirugiaId, Long intervencionId, IntervencionDto intervencion) {
        Intervencion entity = intervencionRepository.findById(intervencionId)
            .orElseThrow(() -> new RuntimeException("Intervencion not found"));
        entity.setObservaciones(intervencion.getObservaciones());
        // Update tipoIntervencion reference if needed
        if (intervencion.getTipoIntervencionId() != null) {
            entity.setTipoIntervencion(tipoIntervencionRepository.findById(intervencion.getTipoIntervencionId())
                .orElseThrow(() -> new RuntimeException("TipoIntervencion not found")));
        }
        Intervencion updated = intervencionRepository.save(entity);
        intervencion = modelMapper.map(updated, IntervencionDto.class); 
        return intervencion;
    }

    @Override
    public void deleteIntervencion(Long cirugiaId, Long intervencionId) {
        intervencionRepository.deleteById(intervencionId);
    }

    private IntervencionDto toDto(Intervencion entity) {
        IntervencionDto dto = new IntervencionDto();
        dto.setId(entity.getId());
        dto.setCirugiaId(entity.getCirugia() != null ? entity.getCirugia().getId() : null);
        dto.setTipoIntervencionId(entity.getTipoIntervencion() != null ? entity.getTipoIntervencion().getId() : null);
        dto.setTipoIntervencionNombre(entity.getTipoIntervencion() != null ? entity.getTipoIntervencion().getNombre() : null);
        dto.setObservaciones(entity.getObservaciones());
        return dto;
    }
}
