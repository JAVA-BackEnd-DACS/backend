package com.dacs.backend.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.QuirofanoDto;
import com.dacs.backend.model.entity.Quirofano;
import com.dacs.backend.service.QuirofanoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/quirofano")
public class QuirofanoController {

    @Autowired
    private QuirofanoService quirofanoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<QuirofanoDto>> getAll() {
        List<Quirofano> quirofanos = quirofanoService.getAll();
        List<QuirofanoDto> quirofanoDtos = quirofanos.stream()
                .map(quirofano -> modelMapper.map(quirofano, QuirofanoDto.class))
                .toList();
        return new ResponseEntity<>(quirofanoDtos, HttpStatus.OK);
    }
    

    @PostMapping("")
    public ResponseEntity<QuirofanoDto> create(@RequestBody QuirofanoDto quirofanoDto) {
        Quirofano quirofano = modelMapper.map(quirofanoDto, Quirofano.class);
        QuirofanoDto data = modelMapper.map(quirofanoService.save(quirofano), QuirofanoDto.class);
        return new ResponseEntity<QuirofanoDto>(data, HttpStatus.OK);
    }
}
