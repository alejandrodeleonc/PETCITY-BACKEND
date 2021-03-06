package Encapsulaciones;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="PERRO")
public class Perro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_perro;
    @Column(name="RFID_CODE",nullable=false,unique=true)
    private String rfid_code;

    private String nombre;
    private Date fecha_registro;
    private int limite_repeticion_comida;


    private boolean adoptado;
    private Date fecha_adopcion;

    @OneToOne(cascade = {CascadeType.ALL})
    private Foto foto;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<PerroVacuna> vacunas;

    private Boolean perdido;

    public Boolean getPerdido() {
        return perdido;
    }

    public void setPerdido(Boolean perdido) {
        this.perdido = perdido;
    }

    public Perro(String rfid_code, String nombre, Date fecha_registro, int limite_repeticion_comida) {
        this.rfid_code = rfid_code;
        this.nombre = nombre;
        this.fecha_registro = fecha_registro;
        this.limite_repeticion_comida = limite_repeticion_comida;
        this.perdido = false;
        this.vacunas = new ArrayList<PerroVacuna>();
        this.foto = null;

    }

    public int getId_perro() {
        return id_perro;
    }



    public String getRfid_code() {
        return rfid_code;
    }

    public void setRfid_code(String rfid_code) {
        this.rfid_code = rfid_code;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public List<PerroVacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<PerroVacuna> vacunas) {
        this.vacunas = vacunas;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getLimite_repeticion_comida() {
        return limite_repeticion_comida;
    }

    public void setId_perro(int id_perro) {
        this.id_perro = id_perro;
    }

    public boolean isAdoptado() {
        return adoptado;
    }

    public void setAdoptado(boolean adoptado) {
        this.adoptado = adoptado;
    }

    public Date getFecha_adopcion() {
        return fecha_adopcion;
    }

    public void setFecha_adopcion(Date fecha_adopcion) {
        this.fecha_adopcion = fecha_adopcion;
    }

    public void addVacuna(PerroVacuna vacuna ){
        this.vacunas.add(vacuna);

    }

    public void setLimite_repeticion_comida(int limite_repeticion_comida) {
        this.limite_repeticion_comida = limite_repeticion_comida;
    }

    public Perro() {
    }


}
