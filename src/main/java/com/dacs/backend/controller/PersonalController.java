package com.dacs.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.query.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.PaginationDto;
import com.dacs.backend.dto.PersonalDto;
import com.dacs.backend.model.entity.Personal;
import com.dacs.backend.model.repository.PersonalRepository;
import com.dacs.backend.service.PersonalService;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;


    @GetMapping("")
    public PaginationDto<PersonalDto.Response> get(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "16") int size,
            @RequestParam(name = "search", required = false) String search) {

        return personalService.getAll(page, size, search);
    }

    @PostMapping("")
    public ResponseEntity<PersonalDto.Response> create(@RequestBody PersonalDto.Create data) {
        PersonalDto.Response out = personalService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalDto.Response> update(@PathVariable Long id, @RequestBody PersonalDto.Update data) {
        PersonalDto.Response out = personalService.update(id, data);
        return ResponseEntity.status(HttpStatus.OK).body(out);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personalService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}