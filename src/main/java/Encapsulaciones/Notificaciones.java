package Encapsulaciones;


import com.sun.istack.NotNull;
import kong.unirest.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name="NOTIFICACIONES")
public class Notificaciones implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_notificaciones;

    @NotNull
    private String contenido;
    @NotNull
    private Date fecha_creacion;
    @NotNull
    private int tipo;

    private boolean estado;

    private boolean activa;

    public Notificaciones() {
    }

    public Notificaciones(String contenido, Date fecha_creacion, int tipo) {

        this.contenido = contenido;
        this.fecha_creacion = fecha_creacion;
        this.estado = false;
        this.tipo = tipo;
        this.activa = true;
    }

    public int getId_notificaciones() {
        return id_notificaciones;
    }

    public void setId_notificaciones(int id_notificaciones) {
        this.id_notificaciones = id_notificaciones;
    }


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha_creacion() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return fecha_creacion.toString();
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
