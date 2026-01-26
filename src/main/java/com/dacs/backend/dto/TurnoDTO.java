package com.dacs.backend.dto;

import java.time.LocalDateTime;

public class TurnoDTO {
    private Long id;
    private LocalDateTime fechaHoraInicio;
    private String estado;
    private Long quirofanoId;
    private Long cirugiaId;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getQuirofanoId() { return quirofanoId; }
    public void setQuirofanoId(Long quirofanoId) { this.quirofanoId = quirofanoId; }

    public Long getCirugiaId() { return cirugiaId; }
    public void setCirugiaId(Long cirugiaId) { this.cirugiaId = cirugiaId; }
}
