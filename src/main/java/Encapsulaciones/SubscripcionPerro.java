package Encapsulaciones;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="SUBCRIPCION_PERRO")
public class SubscripcionPerro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_personaperro;



    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Perro perro;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Subscripcion subscripcion;


    public Subscripcion getSubscripcion() {
        return subscripcion;
    }

    public void setSubscripcion(Subscripcion subscripcion) {
        this.subscripcion = subscripcion;
    }

    private boolean perdido;

    public SubscripcionPerro() {
    }

    public SubscripcionPerro(Subscripcion subscripcion,Perro perro, boolean perdido) {
        this.subscripcion = subscripcion;
        this.perro = perro;
        this.perdido = perdido;
    }


    public Perro getPerro() {
        return perro;
    }

    public void setPerro(Perro perro) {
        this.perro = perro;
    }

    public boolean isPerdido() {
        return perdido;
    }

    public void setPerdido(boolean perdido) {
        this.perdido = perdido;
    }
}
