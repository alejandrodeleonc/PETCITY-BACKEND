package Encapsulaciones;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="HISTORIAL_DE_FACTURACION")
public class Factura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_factura;

    @ManyToOne
    private Persona persona;

    @NotNull
    private Date fecha;

    @OneToOne
    @NotNull
    private Subscripcion subscripcion;

    @NotNull
    private float monto;

    public Factura() {
    }

    public Factura(Persona persona,Date fecha, Subscripcion subscripcion, float monto) {
        this.persona = persona;
        this.fecha = fecha;
        this.subscripcion = subscripcion;
        this.monto = monto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Subscripcion getSubscripcion() {
        return subscripcion;
    }

    public void setSubscripcion(Subscripcion subscripcion) {
        this.subscripcion = subscripcion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
}
