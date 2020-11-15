package Encapsulaciones;


import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="NOTIFICACIONES")
public class Notificaciones implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_notificaciones;

    @ManyToOne
    @JoinColumn(name="id_persona", nullable=false)
    private Persona referencia;

    @NotNull
    private String contenido;
    @NotNull
    private Date fecha_creacion;

    private boolean estado;


    public Notificaciones() {
    }

    public Notificaciones(Persona referencia, String contenido, Date fecha_creacion) {
        this.referencia = referencia;
        this.contenido = contenido;
        this.fecha_creacion = fecha_creacion;
        this.estado = false;
    }

    public int getId_notificaciones() {
        return id_notificaciones;
    }

    public void setId_notificaciones(int id_notificaciones) {
        this.id_notificaciones = id_notificaciones;
    }

    public Persona getReferencia() {
        return referencia;
    }

    public void setReferencia(Persona referencia) {
        this.referencia = referencia;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
