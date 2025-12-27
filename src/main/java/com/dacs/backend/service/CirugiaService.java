package com.dacs.backend.service;

import com.dacs.backend.model.entity.Cirugia;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.PageResponse;
import com.dacs.backend.dto.ServicioDto;

public interface CirugiaService extends CommonService<Cirugia> {

    List<MiembroEquipoMedicoDto.Response> saveEquipoMedico(Long id, List<MiembroEquipoMedicoDto.Create> entity);

    List<MiembroEquipoMedicoDto.Response> getEquipoMedico(Long cirugiaId);

    PageResponse<CirugiaDTO.Response> get(int page, int size);

    List<CirugiaDTO.Response> getBetweenDates(LocalDate fecha, LocalDate fecha2);

    List<ServicioDto> getServicios();

    CirugiaDTO.Response create(CirugiaDTO.Request cirugiaRequestDto);

    CirugiaDTO.Response update(Long id, CirugiaDTO.Request cirugiaDto);

    
}
