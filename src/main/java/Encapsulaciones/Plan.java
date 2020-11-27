package Encapsulaciones;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PLAN")
public class Plan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_plan;
    private String nombre;
    private float costo;
    private int meses_actividad;
    private int cantidad_maxima_de_perros;
//    @OneToMany(fetch = FetchType.EAGER)
//    @Fetch(value = FetchMode.SUBSELECT)
//    private List<Subscripcion> subcripciones;
    public Plan() {
    }

    public Plan(String nombre, float costo, int meses_actividad, int cantidad_maxima_de_perros) {
        this.nombre = nombre;
        this.costo = costo;
        this.meses_actividad = meses_actividad;
        this.cantidad_maxima_de_perros = cantidad_maxima_de_perros;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public int getMeses_actividad() {
        return meses_actividad;
    }

    public void setMeses_actividad(int meses_actividad) {
        this.meses_actividad = meses_actividad;
    }

    public int getCantidad_maxima_de_perros() {
        return cantidad_maxima_de_perros;
    }

    public void setCantidad_maxima_de_perros(int cantidad_maxima_de_perros) {
        this.cantidad_maxima_de_perros = cantidad_maxima_de_perros;
    }
}
