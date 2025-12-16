package com.dacs.backend.dto;

import lombok.Data;

@Data
public class ServicioDto {
    private Long id;
    private String nombre;
    private Integer duracionMinutos;
}
