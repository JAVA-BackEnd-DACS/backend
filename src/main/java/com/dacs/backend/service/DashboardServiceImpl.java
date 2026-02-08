package com.dacs.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dacs.backend.dto.EstadisticasGeneralesDto;

@Service
public class DashboardServiceImpl  implements DashboardService {

    @Autowired
    private CirugiaService cirugiaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private QuirofanoService quirofanoService;

    
    @Override
    public EstadisticasGeneralesDto obtenerEstadisticasGenerales() {

        EstadisticasGeneralesDto stats = new EstadisticasGeneralesDto();
        stats.setTotalPacientes(pacienteService.countPacientes());
        stats.setCirugiasHoy(cirugiaService.countCirugiasRestantesHoy());
        stats.setCirugiasEstaSemana(cirugiaService.countCirugiasEstaSemana());
        stats.setQuirofanosDisponibles(quirofanoService.quirofanosDisponibles());
        stats.setQuirofanosEnUso(quirofanoService.quirofanosEnUso());
        return stats;

    }
}
