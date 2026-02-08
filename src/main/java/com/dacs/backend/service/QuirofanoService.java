package com.dacs.backend.service;

import java.util.List;

import com.dacs.backend.model.entity.Quirofano;

public interface QuirofanoService extends CommonService<Quirofano>{

    List<Quirofano> quirofanosEnUso();

    List<Quirofano> quirofanosDisponibles();


}
