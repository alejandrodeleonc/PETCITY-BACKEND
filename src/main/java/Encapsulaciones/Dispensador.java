package Encapsulaciones;

import org.hibernate.type.StringNVarcharType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="DISPENSADOR")
public class Dispensador implements Serializable {

    @Id
    private String id_dispensador;
    private String longitud;
    private String latitud;
    private String direccion;

    public Dispensador() {
    }

    public Dispensador(String id_dispensador, String longitud, String latitud, String direccion) {
        this.id_dispensador = id_dispensador;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
    }

    public String getId_dispensador() {
        return id_dispensador;
    }

    public void setId_dispensador(String id_dispensador) {
        this.id_dispensador = id_dispensador;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
