package com.parcial.saberpro.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "resoluciones_beneficios")
public class ResolucionBeneficio {

    @Id
    private String id;
    private String numeroResolucion;
    private String titulo;
    private String descripcion;
    private String area;
    private LocalDate fechaEmision;
    private LocalDate fechaVigencia;
    private Double puntajeMinimo;
    private List<String> modulosAplicables;
    private String tipoBeneficio;
    private String porcentajeDescuento;
    private boolean vigente;
    private String creadoPor;
    private LocalDateTime fechaCreacion;

    public ResolucionBeneficio() {
        this.vigente = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumeroResolucion() { return numeroResolucion; }
    public void setNumeroResolucion(String numeroResolucion) { this.numeroResolucion = numeroResolucion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaVigencia() { return fechaVigencia; }
    public void setFechaVigencia(LocalDate fechaVigencia) { this.fechaVigencia = fechaVigencia; }

    public Double getPuntajeMinimo() { return puntajeMinimo; }
    public void setPuntajeMinimo(Double puntajeMinimo) { this.puntajeMinimo = puntajeMinimo; }

    public List<String> getModulosAplicables() { return modulosAplicables; }
    public void setModulosAplicables(List<String> modulosAplicables) { this.modulosAplicables = modulosAplicables; }

    public String getTipoBeneficio() { return tipoBeneficio; }
    public void setTipoBeneficio(String tipoBeneficio) { this.tipoBeneficio = tipoBeneficio; }

    public String getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(String porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public boolean isVigente() { return vigente; }
    public void setVigente(boolean vigente) { this.vigente = vigente; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}