package Encapsulaciones;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ROL")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_rol;

    private String nombre;
    private boolean activo;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "ROL_PERMISO",
            joinColumns = { @JoinColumn(name = "id_permiso") },
            inverseJoinColumns = { @JoinColumn(name = "id_rol") }

    )
    List<Permiso> permisos = new ArrayList<Permiso>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "PERSONA_ROL",
            joinColumns = { @JoinColumn(name = "id_persona") },
            inverseJoinColumns = { @JoinColumn(name = "id_rol") }
    )
    private List<Persona> personasroles = new ArrayList<Persona>();
    public Rol(String nombre, boolean activo) {
        this.nombre = nombre;
        this.activo = activo;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
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
