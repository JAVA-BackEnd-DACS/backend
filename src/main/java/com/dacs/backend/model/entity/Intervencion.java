package com.dacs.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


// NO LO USAMOS EN DACS
@Entity
@Data
public class Intervencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cirugia_id", nullable = false)
    private Cirugia cirugia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_intervencion_id", nullable = false)
    private TipoIntervencion tipoIntervencion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Getters y setters
}