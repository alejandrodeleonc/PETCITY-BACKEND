package Encapsulaciones;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="SECTOR")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_sector;

    private String nombre;

    private String latitud;
    private String longitud;

    private int radio;

    private int cantidadDispensadores;
    private boolean estado;
    private String color;

    public String ultimaIp;

    private boolean existe;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<HistorialSectores> historialSectores;

    public Sector() {
    }

    public Sector(String nombre, String latitud, String longitud, int radio, String color) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.radio = radio;
        this.cantidadDispensadores = 0;
        this.estado = false;
        this.color = color;
        this.existe = true;
        this.historialSectores = new ArrayList<HistorialSectores>();
        this.ultimaIp = "";
    }

    public int getCantidadDispensadores() {
        return cantidadDispensadores;
    }
    public void addDispensador(){
        this.cantidadDispensadores++;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setCantidadDispensadores(int cantidadDispensadores) {
        this.cantidadDispensadores = cantidadDispensadores;
    }

    public int getId_sector() {
        return id_sector;
    }

    public void setId_sector(int id_sector) {
        this.id_sector = id_sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    public List<HistorialSectores> getHistorialSectores() {
        return historialSectores;
    }

    public void setHistorialSectores(List<HistorialSectores> historialSectores) {
        this.historialSectores = historialSectores;
    }

    public void addRegistroAHistorial(HistorialSectores sectorRegistro ){
        this.historialSectores.add(sectorRegistro);
    }

    public String getUltimaIp() {
        return ultimaIp;
    }

    public void setUltimaIp(String ultimaIp) {
        this.ultimaIp = ultimaIp;
    }
}
