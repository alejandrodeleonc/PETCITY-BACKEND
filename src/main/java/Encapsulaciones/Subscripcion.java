package Encapsulaciones;

import javax.persistence.*;
import org.hibernate.annotations.Fetch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="SUBSCRIPCION")
public class Subscripcion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_subscripcion;
    private boolean pago;
    private float monto;
    @ManyToOne
    @JoinColumn( name="id_plan")
    private Plan plan;

//    @ManyToOne
//    @JoinColumn(name="id_persona", nullable=false)
//    private Persona persona;

    private Date fechaVencimientoPago;


    public Subscripcion() {
    }

    public Subscripcion(Plan plan, Date fechaVencimientoPago) {
        this.plan = plan;
//        this.persona = persona;
        this.fechaVencimientoPago = fechaVencimientoPago;
    }

    public int getId_subscripcion() {
        return id_subscripcion;
    }

    public void setId_subscripcion(int id_subscripcion) {
        this.id_subscripcion = id_subscripcion;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

//    public Persona getPersona() {
//        return persona;
//    }
//
//    public void setPersona(Persona persona) {
//        this.persona = persona;
//    }

    public Date getFechaVencimientoPago() {
        return fechaVencimientoPago;
    }

    public void setFechaVencimientoPago(Date fechaVencimientoPago) {
        this.fechaVencimientoPago = fechaVencimientoPago;
    }
}
