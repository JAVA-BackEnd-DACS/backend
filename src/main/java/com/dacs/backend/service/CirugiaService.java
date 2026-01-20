package com.dacs.backend.service;

import com.dacs.backend.model.entity.Cirugia;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.PaginationDto;
import com.dacs.backend.dto.ServicioDto;

public interface CirugiaService extends CommonService<Cirugia> {

    List<MiembroEquipoMedicoDto.Response> saveEquipoMedico(Long id, List<MiembroEquipoMedicoDto.Create> entity);

    List<MiembroEquipoMedicoDto.Response> getEquipoMedico(Long cirugiaId);

    PaginationDto<CirugiaDTO.Response> getCirugias(int page, int size, LocalDate fechaInicio, LocalDate fechaFin);

    List<ServicioDto> getServicios();

    CirugiaDTO.Response createCirugia(CirugiaDTO.Create cirugiaRequestDto);

    CirugiaDTO.Response updateCirugia(Long id, CirugiaDTO.Update cirugiaDto);
}
