package com.dacs.backend.service;

import java.util.List;

import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.dto.PacienteDTO.Request;
import com.dacs.backend.dto.PacienteDTO.Response;
import com.dacs.backend.dto.PageResponse;
import com.dacs.backend.model.entity.Paciente;

public interface PacienteService extends CommonService<Paciente>{

    List<Paciente> getByDni(String dni);

    List<Response> getPacientesByIds(List<Long> ids);

    PageResponse<Response> getByPage(int page, int size, String search);

    Response crear(Request pacienteDTO);


}
