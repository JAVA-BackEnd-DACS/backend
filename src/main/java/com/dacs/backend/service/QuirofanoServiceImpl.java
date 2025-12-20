package com.dacs.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.model.repository.QuirofanoRepository;

@Service
public class QuirofanoServiceImpl implements QuirofanoService {

    @Autowired
    QuirofanoRepository quirofanoRepository;

    @Override
    public Optional<Quirofano> getById(Long id) {
        return quirofanoRepository.findById(id);
    }

    @Override
    public Quirofano save(Quirofano entity) {
        return quirofanoRepository.save(entity);
    }

    @Override
    public Boolean existById(Long id) {
        return quirofanoRepository.existsById(id);
    }

    @Override
    public java.util.List<Quirofano> getAll() {
        return quirofanoRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Optional<Quirofano> quirofano = getById(id);
        quirofanoRepository.delete(quirofano.get());
    }

    @Override
    public Quirofano getBy(java.util.Map<String, Object> filter) {
        throw new UnsupportedOperationException();
    }
}
