package com.dacs.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.service.CirugiaService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(value = "/cirugia")
public class CirugiaController {

    @Autowired
    private CirugiaService cirugiaService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("")
    public CirugiaDTO postMethodName(@RequestBody CirugiaDTO cirugiaDTO) {
        System.out.println(cirugiaDTO);
        com.dacs.backend.model.entity.Cirugia toSave = modelMapper.map(cirugiaDTO, com.dacs.backend.model.entity.Cirugia.class);
        System.out.println(toSave);
        com.dacs.backend.model.entity.Cirugia saved = cirugiaService.save(toSave);
        cirugiaDTO.setId(saved.getId());
        return cirugiaDTO;
    }
}
