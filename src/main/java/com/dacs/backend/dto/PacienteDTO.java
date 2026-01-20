package com.dacs.backend.dto;

import java.sql.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PacienteDTO {

    @Data
    static public class Create{
        private Long id;
        private String nombre;
        private String apellido;
        private String dni;
        private Date fecha_nacimiento;
        private String altura;
        private String peso;
        private String direccion;
        private String telefono;
    }
    
    @Data
    static public class Response {
        private Long id;
        private String nombre;
        private String apellido;
        private String dni;
        private Date fecha_nacimiento;
        private String altura;
        private String peso;
        private String direccion;
        private String telefono;
    }

    @Getter
    @Setter
    static public class Update extends Create {
    }
}
