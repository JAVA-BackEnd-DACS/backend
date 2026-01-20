package com.dacs.backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.dto.PaginationDto;
import com.dacs.backend.model.entity.Paciente;
import com.dacs.backend.service.PacienteService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/pacient")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping("")
    public ResponseEntity<PacienteDTO.Response> create(@RequestBody PacienteDTO.Create pacienteDTO) {
        PacienteDTO.Response data = pacienteService.creatPaciente(pacienteDTO);
        return new ResponseEntity<PacienteDTO.Response>(data, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<PaginationDto<PacienteDTO.Response>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "16") Integer size,
            @RequestParam(name = "search", required = false) String search) {
        
        return new ResponseEntity<>(
            pacienteService.getByPage(page, size, search), 
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        pacienteService.delete(id);
        if (!pacienteService.existById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
            
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
