package com.parcial.saberpro.entity;

import java.time.LocalDate;

public class ResultadoSaberPro {

    private LocalDate fechaPresentacion;
    private String periodo;
    private Double razonamientoCuantitativo;
    private Double lecturasCritica;
    private Double comunicacionEscrita;
    private Double competenciasCiudadanas;
    private Double ingles;
    private String moduloEspecificoNombre;
    private Double moduloEspecificoPuntaje;
    private Double puntajeGlobal;
    private String clasificacion;
    private String observaciones;

    public ResultadoSaberPro() {}

    public ResultadoSaberPro(LocalDate fechaPresentacion, String periodo, Double razonamientoCuantitativo,
                              Double lecturasCritica, Double comunicacionEscrita, Double competenciasCiudadanas,
                              Double ingles, String moduloEspecificoNombre, Double moduloEspecificoPuntaje,
                              Double puntajeGlobal, String clasificacion, String observaciones) {
        this.fechaPresentacion = fechaPresentacion;
        this.periodo = periodo;
        this.razonamientoCuantitativo = razonamientoCuantitativo;
        this.lecturasCritica = lecturasCritica;
        this.comunicacionEscrita = comunicacionEscrita;
        this.competenciasCiudadanas = competenciasCiudadanas;
        this.ingles = ingles;
        this.moduloEspecificoNombre = moduloEspecificoNombre;
        this.moduloEspecificoPuntaje = moduloEspecificoPuntaje;
        this.puntajeGlobal = puntajeGlobal;
        this.clasificacion = clasificacion;
        this.observaciones = observaciones;
    }

    public LocalDate getFechaPresentacion() { return fechaPresentacion; }
    public void setFechaPresentacion(LocalDate fechaPresentacion) { this.fechaPresentacion = fechaPresentacion; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Double getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Double razonamientoCuantitativo) { this.razonamientoCuantitativo = razonamientoCuantitativo; }

    public Double getLecturasCritica() { return lecturasCritica; }
    public void setLecturasCritica(Double lecturasCritica) { this.lecturasCritica = lecturasCritica; }

    public Double getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(Double comunicacionEscrita) { this.comunicacionEscrita = comunicacionEscrita; }

    public Double getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Double competenciasCiudadanas) { this.competenciasCiudadanas = competenciasCiudadanas; }

    public Double getIngles() { return ingles; }
    public void setIngles(Double ingles) { this.ingles = ingles; }

    public String getModuloEspecificoNombre() { return moduloEspecificoNombre; }
    public void setModuloEspecificoNombre(String moduloEspecificoNombre) { this.moduloEspecificoNombre = moduloEspecificoNombre; }

    public Double getModuloEspecificoPuntaje() { return moduloEspecificoPuntaje; }
    public void setModuloEspecificoPuntaje(Double moduloEspecificoPuntaje) { this.moduloEspecificoPuntaje = moduloEspecificoPuntaje; }

    public Double getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(Double puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getClasificacionBadge() {
        if (clasificacion == null) return "secondary";
        return switch (clasificacion) {
            case "SUPERIOR" -> "success";
            case "ALTO"     -> "primary";
            case "MEDIO"    -> "warning";
            case "BAJO"     -> "danger";
            default         -> "secondary";
        };
    }
}