package Encapsulaciones;



import com.sun.istack.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="PERSONA")
public class Persona  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_persona;
    @NotNull
    private String nombre;

    @NotNull
    private String identificacion;

    @NotNull
    private Date fecha_nacimiento;

    @NotNull
    private String direccion;

    @NotNull
    private String usuario;

    @NotNull
    private String password;

    @NotNull
    private String Codigo_Retiro;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="persona")
    private List<Subscripcion> subcripciones;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="referencia")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Notificaciones> notificaciones;

    @ManyToMany( mappedBy = "personasroles")
    List<Rol> rolespersona = new ArrayList<Rol>();
    public Persona() {
    }

    public Persona(String nombre, String identificacion, Date fecha_nacimiento, String direccion, String usuario, String password, String codigo_Retiro) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.fecha_nacimiento = fecha_nacimiento;
        this.direccion = direccion;
        this.usuario = usuario;
        this.password = password;
        Codigo_Retiro = codigo_Retiro;
        this.subcripciones = new ArrayList<>();
    }

    public List<Notificaciones> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificaciones> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigo_Retiro() {
        return Codigo_Retiro;
    }

    public void setCodigo_Retiro(String codigo_Retiro) {
        Codigo_Retiro = codigo_Retiro;
    }

    public List<Subscripcion> getSubcripciones() {
        return subcripciones;
    }

    public void setSubcripciones(List<Subscripcion> subcripciones) {
        this.subcripciones = subcripciones;
    }
}
