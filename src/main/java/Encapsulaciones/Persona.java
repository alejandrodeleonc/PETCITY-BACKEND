package Encapsulaciones;



import com.sun.istack.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="PERSONA")
public class Persona implements Serializable   {
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


    @Column(nullable=false,unique=true)
    private String usuario;

    @NotNull
    private String password;
    @NotNull
    private String correo;

    @NotNull
    private String Codigo_Retiro;

    @OneToOne(fetch = FetchType.EAGER)
    private Subscripcion subcripciones;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Notificaciones> notificaciones;

//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Factura> historial_de_facturacion;

//    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "personasroles")
//    @Fetch(value = FetchMode.SUBSELECT)
//    List<Rol> rolespersona = new ArrayList<Rol>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "PERSONA_ROL",
            joinColumns= { @JoinColumn(name = "id_persona ") },
            inverseJoinColumns= { @JoinColumn(name = "id_rol") }
    )
    private List<Rol> personasroles;



    @Lob
    @OneToOne(fetch = FetchType.EAGER)
    private Foto foto;

    public Persona() {
    }

    public Persona(String nombre, String identificacion, String correo, Date fecha_nacimiento, String direccion, String usuario, String password, String codigo_Retiro) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.direccion = direccion;
        this.usuario = usuario;
        this.password = password;
        this.Codigo_Retiro = codigo_Retiro;
        this.subcripciones = null;
        this.notificaciones =  new ArrayList<Notificaciones>();
        this.personasroles =  new ArrayList<Rol>();
//        this.historial_de_facturacion =  new ArrayList<Factura>();

    }

    public List<Rol> getPersonasroles() {
        return personasroles;
    }

//    public void addRol(Rol rol){
//        this.personasroles.add(rol);
//    }

    public void setPersonasroles(List<Rol> personasroles) {
        this.personasroles = personasroles;
    }

    public void addRol(Rol rol){
        this.personasroles.add(rol);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public Subscripcion getSubcripciones() {
        return subcripciones;
    }

    public void setSubcripciones(Subscripcion subcripciones) {
        this.subcripciones = subcripciones;
    }

//    public List<Rol> getRolespersona() {
//        return rolespersona;
//    }
//
//    public void setRolespersona(List<Rol> rolespersona) {
//        this.rolespersona = rolespersona;
//    }

    public Foto getFoto() {
        return foto;
    }

    public void addNotificacion(Notificaciones not){
        this.notificaciones.add(not);
    }

//    public List<Factura> getHistorial_de_facturacion() {
//        return historial_de_facturacion;
//    }
//
//    public void setHistorial_de_facturacion(List<Factura> historial_de_facturacion) {
//        this.historial_de_facturacion = historial_de_facturacion;
//    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

//    public void addSubscripcion(Subscripcion subscripcion){
//        this.subcripciones.add(subscripcion);
//    }





}
