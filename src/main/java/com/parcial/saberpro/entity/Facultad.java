package com.parcial.saberpro.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Document(collection = "facultades")
public class Facultad {

    @Id
    private String id;

    @NotBlank(message = "El nombre de la facultad es obligatorio")
    private String nombre;

    private String codigo;
    private String descripcion;
    private String directorId;
    private String directorNombre;
    private boolean activa;
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public Facultad() {
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor completo
    public Facultad(String id, String nombre, String codigo, String descripcion,
                    String directorId, String directorNombre, boolean activa,
                    LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.directorId = directorId;
        this.directorNombre = directorNombre;
        this.activa = activa;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDirectorId() { return directorId; }
    public void setDirectorId(String directorId) { this.directorId = directorId; }

    public String getDirectorNombre() { return directorNombre; }
    public void setDirectorNombre(String directorNombre) { this.directorNombre = directorNombre; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return "Facultad{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", activa=" + activa +
                '}';
    }
}