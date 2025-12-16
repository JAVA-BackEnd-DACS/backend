package com.dacs.backend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CirugiaDTO {

    @Data
    static public class Response {
        private Long id;
        private String prioridad;
        private LocalDateTime fecha_hora_inicio;
        private String estado;
        private String anestesia;
        private String tipo;
        private ServicioDto servicio;
        private PacienteDTO.Response paciente;
        private QuirofanoDto quirofano;
    }

    @Data
    static public class Request {
        private String prioridad;
        private LocalDateTime fecha_hora_inicio;
        private String estado;
        private String anestesia;
        private String tipo;
        private Long pacienteId;
        private Long servicioId;
        private Long quirofanoId;
    }
}
