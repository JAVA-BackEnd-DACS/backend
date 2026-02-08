package com.dacs.backend.service;

import java.util.List;

import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.dto.PacienteDTO.Create;
import com.dacs.backend.dto.PacienteDTO.Response;
import com.dacs.backend.dto.PacienteDTO.Update;
import com.dacs.backend.dto.PaginacionDto;
import com.dacs.backend.model.entity.Paciente;

public interface PacienteService extends CommonService<Paciente>{

    List<Paciente> getByDni(String dni);

    List<Response> getPacientesByIds(List<Long> ids);

    PaginacionDto.Response<Response> getByPage(int page, int size, String search);

    Response creatPaciente(Create pacienteDTO);
    
    Response updatePaciente(Update pacienteDTO);

    long countPacientes();
}
