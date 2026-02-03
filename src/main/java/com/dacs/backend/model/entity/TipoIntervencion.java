package com.dacs.backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TipoIntervencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
