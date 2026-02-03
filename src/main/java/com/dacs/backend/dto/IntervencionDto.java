package com.dacs.backend.dto;

import lombok.Data;

@Data
public class IntervencionDto {

  private Long id;
  private Long cirugiaId;
  private Long tipoIntervencionId;
  private String tipoIntervencionNombre;
  private String observaciones;

}


