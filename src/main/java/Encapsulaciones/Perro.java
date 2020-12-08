package Encapsulaciones;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PERRO")
public class Perro implements Serializable {
    @Id
    private String id_perro;

    private String nombre;
    private String fecha_registro;
    private int limite_repeticion_comida;


//    @OneToMany
//    private List<PerroVacuna> vacunas;

    private Boolean perdido;

    public Boolean getPerdido() {
        return perdido;
    }

    public void setPerdido(Boolean perdido) {
        this.perdido = perdido;
    }

    public Perro(String id_perro, String nombre, String fecha_registro, int limite_repeticion_comida) {
        this.id_perro = id_perro;
        this.nombre = nombre;
        this.fecha_registro = fecha_registro;
        this.limite_repeticion_comida = limite_repeticion_comida;
        this.perdido = false;
//        this.vacunas = new ArrayList<PerroVacuna>();

    }


//    public List<PerroVacuna> getVacunas() {
//        return vacunas;
//    }

//    public void setVacunas(List<PerroVacuna> vacunas) {
//        this.vacunas = vacunas;
//    }

    public String getId_perro() {
        return id_perro;
    }

    public void setId_perro(String id_perro) {
        this.id_perro = id_perro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getLimite_repeticion_comida() {
        return limite_repeticion_comida;
    }

    public void setLimite_repeticion_comida(int limite_repeticion_comida) {
        this.limite_repeticion_comida = limite_repeticion_comida;
    }

    public Perro() {
    }


}
