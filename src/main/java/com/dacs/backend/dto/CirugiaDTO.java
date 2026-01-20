package com.dacs.backend.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
        private QuirofanoDTO quirofano;
    }

    @Data
    static public class Create {
        private String prioridad;
        private LocalDateTime fecha_hora_inicio;
        private String estado;
        private String anestesia;
        private String tipo;
        private Long pacienteId;
        private Long servicioId;
        private Long quirofanoId;
    }

    @Getter
    @Setter
    static public class Update extends Create {
    }
}
