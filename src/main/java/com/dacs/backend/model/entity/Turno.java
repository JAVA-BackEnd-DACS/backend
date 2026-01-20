package com.dacs.backend.model.entity;

import java.time.LocalDateTime;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cirugia_id")
    @Nullable
    private Cirugia cirugia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quirofano_id", nullable = false)
    private Quirofano quirofano;

    @Column(length = 100, nullable = true)
    private String estado;
}
