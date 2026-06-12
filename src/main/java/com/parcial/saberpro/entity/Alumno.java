package com.parcial.saberpro.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "alumnos")
public class Alumno {

    @Id
    private String id;

    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @Indexed(unique = true)
    @NotBlank
    private String cedula;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;

    private String facultadId;
    private String facultadNombre;
    private String programaAcademico;
    private String semestre;
    private String codigoEstudiante;

    private String estadoSaberPro;
    private boolean aprobadoPorCoordinacion;
    private LocalDateTime fechaAprobacion;
    private String aprobadoPor;

    private String comprobantePagoPath;
    private LocalDateTime fechaCargoPago;
    private boolean pagoVerificado;

    private ResultadoSaberPro resultadoUnico;
    private ResultadoSaberPro resultadoTotal;

    private boolean tieneBeneficio;
    private String tipoBeneficio;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Alumno() {
        this.estadoSaberPro = "PENDIENTE";
        this.aprobadoPorCoordinacion = false;
        this.pagoVerificado = false;
        this.tieneBeneficio = false;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getFacultadId() { return facultadId; }
    public void setFacultadId(String facultadId) { this.facultadId = facultadId; }

    public String getFacultadNombre() { return facultadNombre; }
    public void setFacultadNombre(String facultadNombre) { this.facultadNombre = facultadNombre; }

    public String getProgramaAcademico() { return programaAcademico; }
    public void setProgramaAcademico(String programaAcademico) { this.programaAcademico = programaAcademico; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

    public String getEstadoSaberPro() { return estadoSaberPro; }
    public void setEstadoSaberPro(String estadoSaberPro) { this.estadoSaberPro = estadoSaberPro; }

    public boolean isAprobadoPorCoordinacion() { return aprobadoPorCoordinacion; }
    public void setAprobadoPorCoordinacion(boolean aprobadoPorCoordinacion) { this.aprobadoPorCoordinacion = aprobadoPorCoordinacion; }

    public LocalDateTime getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(LocalDateTime fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }

    public String getAprobadoPor() { return aprobadoPor; }
    public void setAprobadoPor(String aprobadoPor) { this.aprobadoPor = aprobadoPor; }

    public String getComprobantePagoPath() { return comprobantePagoPath; }
    public void setComprobantePagoPath(String comprobantePagoPath) { this.comprobantePagoPath = comprobantePagoPath; }

    public LocalDateTime getFechaCargoPago() { return fechaCargoPago; }
    public void setFechaCargoPago(LocalDateTime fechaCargoPago) { this.fechaCargoPago = fechaCargoPago; }

    public boolean isPagoVerificado() { return pagoVerificado; }
    public void setPagoVerificado(boolean pagoVerificado) { this.pagoVerificado = pagoVerificado; }

    public ResultadoSaberPro getResultadoUnico() { return resultadoUnico; }
    public void setResultadoUnico(ResultadoSaberPro resultadoUnico) { this.resultadoUnico = resultadoUnico; }

    public ResultadoSaberPro getResultadoTotal() { return resultadoTotal; }
    public void setResultadoTotal(ResultadoSaberPro resultadoTotal) { this.resultadoTotal = resultadoTotal; }

    public boolean isTieneBeneficio() { return tieneBeneficio; }
    public void setTieneBeneficio(boolean tieneBeneficio) { this.tieneBeneficio = tieneBeneficio; }

    public String getTipoBeneficio() { return tipoBeneficio; }
    public void setTipoBeneficio(String tipoBeneficio) { this.tipoBeneficio = tipoBeneficio; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}