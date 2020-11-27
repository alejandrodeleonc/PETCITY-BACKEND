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

    public Map<String, Object> getReferencia() {
        Map<String, Object> json = new HashMap();
        json.put("id_persona", referencia.getId_persona());
        json.put("nombre", referencia.getNombre());
        return json;
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

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
