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
    @ManyToOne(cascade = CascadeType.ALL)
    private Sector sector=null;

    private boolean estado=false;

    private boolean refil=false;

    private boolean existe=true;

    public Dispensador() {
    }

    public Dispensador(String dispensador, String longitud, String latitud, String direccion, Sector sector) {
        this.dispensador = dispensador;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
        this.sector = sector;
        this.estado = true;
        this.refil = false;
        this.existe = true;
    }

    public int getId_dispensador() {
        return id_dispensador;
    }

    public void setId_dispensador(int id_dispensador) {
        this.id_dispensador = id_dispensador;
    }

    public String getDispensador() {
        return dispensador;
    }

    public void setDispensador(String dispensador) {
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

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isRefil() {
        return refil;
    }

    public void setRefil(boolean refil) {
        this.refil = refil;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }
}
