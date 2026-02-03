package com.dacs.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacs.backend.dto.TipoIntervencionDto;
import com.dacs.backend.model.repository.TipoIntervencionRepository;

@Service
public class TipoIntervencionServiceImpl implements TipoIntervencionService {

    @Autowired
    TipoIntervencionRepository tipoIntervencionRepository;

    @Override
    public Optional<TipoIntervencionDto> getById(Long id) {
        return Optional.empty();
    }

    @Override   
    public TipoIntervencionDto save(TipoIntervencionDto entity) {
        return null;
    }

    @Override
    public Boolean existById(Long id) {
        return tipoIntervencionRepository.existsById(id);
    }

    @Override
    public java.util.List<TipoIntervencionDto> getAll() {
        return tipoIntervencionRepository.findAll().stream()
            .map(entity -> {
                TipoIntervencionDto dto = new TipoIntervencionDto();
                dto.setId(entity.getId());
                dto.setNombre(entity.getNombre());
                dto.setDescripcion(entity.getDescripcion());
                return dto;
            })
            .toList();
    }
    @Override
    public void delete(Long id) {
        Optional<TipoIntervencionDto> tipoIntervencion = getById(id);
    }

    @Override
    public TipoIntervencionDto getBy(java.util.Map<String, Object> filter) {
        throw new UnsupportedOperationException();
    }
    
}
