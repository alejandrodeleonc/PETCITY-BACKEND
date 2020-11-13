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


    public SubscripcionPerro() {
    }

    public SubscripcionPerro(Subscripcion subscripcion,Perro perro) {
        this.subscripcion = subscripcion;
        this.perro = perro;
    }


    public Perro getPerro() {
        return perro;
    }

    public void setPerro(Perro perro) {
        this.perro = perro;
    }

}
