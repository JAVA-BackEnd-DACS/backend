package com.dacs.backend.service;

import com.dacs.backend.model.entity.Cirugia;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.CirugiaDTO.Response;
import com.dacs.backend.dto.MiembroEquipoMedicoDto;
import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.dto.ServicioDto;

public interface CirugiaService extends CommonService<Cirugia> {

    List<MiembroEquipoMedicoDto.Response> saveEquipoMedico(Long id, List<MiembroEquipoMedicoDto.Create> entity);

    List<MiembroEquipoMedicoDto.Response> getEquipoMedico(Long cirugiaId);

    PaginacionDto<CirugiaDTO.Response> getCirugias(int pagina, int tamaño, LocalDate fechaInicio, LocalDate fechaFin, String estado);

    List<ServicioDto> getServicios(int pagina, int tamaño);

    CirugiaDTO.Response createCirugia(CirugiaDTO.Create cirugiaRequestDto);

    CirugiaDTO.Response updateCirugia(Long id, CirugiaDTO.Update cirugiaDto);

    CirugiaDTO.Response finalizarCirugia(long long1);
}
