package Encapsulaciones;

import org.hibernate.type.StringNVarcharType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="DISPENSADOR")
public class Dispensador implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_dispensador;
    private String dispensador;
    private String longitud;
    private String latitud;
    private String direccion;

    public Dispensador() {
    }

    public Dispensador(String dispensador, String longitud, String latitud, String direccion) {
        this.dispensador = dispensador;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
    }

    public String getId_dispensador() {
        return dispensador;
    }

    public void setId_dispensador(String dispensador) {
        this.dispensador = dispensador;
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
