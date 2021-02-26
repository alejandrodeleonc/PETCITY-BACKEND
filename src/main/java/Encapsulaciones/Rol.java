package Encapsulaciones;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ROL")
public class Rol  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_rol;

    private String nombre;
    private boolean activo;

    @ManyToMany( fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "ROL_ACCION",
            joinColumns = { @JoinColumn(name = "id_accion") },
            inverseJoinColumns = { @JoinColumn(name = "id_rol") }

    )
    private List<Accion> acciones;

    public Rol() {
    }

    public Rol(String nombre, boolean activo) {
        this.nombre = nombre;
        this.activo = activo;
        this.acciones = new ArrayList<Accion>();
    }

    public List<Accion> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }

    public void addAccion(Accion accion){
        this.acciones.add(accion);
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
