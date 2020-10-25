package Encapsulaciones;



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
    private String nombre;
    private int identificacion;
    private Date fecha_nacimiento;
    private String direccion;
    private String usuario;
    private String password;
    private String Codigo_Retiro;

    @OneToMany(mappedBy="persona")
    private List<Subscripcion> subcripciones;


    @ManyToMany(mappedBy = "personasroles")
    List<Rol> rolespersona = new ArrayList<Rol>();
    public Persona() {
    }

    public Persona(String nombre, int identificacion, Date fecha_nacimiento, String direccion, String usuario, String password, String codigo_Retiro) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.fecha_nacimiento = fecha_nacimiento;
        this.direccion = direccion;
        this.usuario = usuario;
        this.password = password;
        Codigo_Retiro = codigo_Retiro;
        this.subcripciones = new ArrayList<>();
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

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
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
