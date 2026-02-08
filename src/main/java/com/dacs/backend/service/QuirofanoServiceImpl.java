package com.dacs.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.model.repository.QuirofanoRepository;
import com.dacs.backend.model.repository.TurnoRepository;
import java.time.LocalDateTime;

@Service
public class QuirofanoServiceImpl implements QuirofanoService {

    @Autowired
    private QuirofanoRepository quirofanoRepository;

    @Autowired
    private TurnoRepository turnoRepository;

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



    public List<Quirofano> quirofanosDisponibles() {
        LocalDateTime now = LocalDateTime.now();
        List<Quirofano> todos = quirofanoRepository.findAll();
        // Solo turnos que están activos en este instante
        List<Long> enUsoIds = turnoRepository.findAllByFechaHoraInicioBetween(
            now, now
        ).stream().map(t -> t.getQuirofano().getId()).distinct().toList();
        return todos.stream().filter(q -> !enUsoIds.contains(q.getId())).toList();
    }


    public List<Quirofano> quirofanosEnUso() {
        LocalDateTime now = LocalDateTime.now();
        List<Long> enUsoIds = turnoRepository.findAllByFechaHoraInicioBetween(
            now, now
        ).stream().map(t -> t.getQuirofano().getId()).distinct().toList();
        return quirofanoRepository.findAll().stream().filter(q -> enUsoIds.contains(q.getId())).toList();
    }
}
