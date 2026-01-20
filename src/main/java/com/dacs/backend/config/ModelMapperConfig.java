package com.dacs.backend.config;

import com.dacs.backend.dto.CirugiaDTO;
import com.dacs.backend.dto.PacienteDTO;
import com.dacs.backend.model.entity.Cirugia;
import com.dacs.backend.model.entity.Paciente;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();

        // Mapeo Cirugia -> CirugiaDTO.Response (sin paciente)
        TypeMap<Cirugia, CirugiaDTO.Response> cirugiaMap = mm.emptyTypeMap(Cirugia.class, CirugiaDTO.Response.class);
        cirugiaMap.addMappings(m -> m.skip(CirugiaDTO.Response::setPaciente));
        cirugiaMap.implicitMappings();
        //??
        // Mapeo CirugiaDTO.Update -> Cirugia (skip id para no sobrescribir el id de la entidad cargada)
        TypeMap<CirugiaDTO.Create, Cirugia> updateToCirugiaMap = mm.emptyTypeMap(CirugiaDTO.Create.class, Cirugia.class);
        updateToCirugiaMap.addMappings(m -> m.skip(Cirugia::setId));
        updateToCirugiaMap.implicitMappings();

        return mm;
    }
}
