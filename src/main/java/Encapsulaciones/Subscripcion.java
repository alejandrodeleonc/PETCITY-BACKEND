package Encapsulaciones;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
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
    @JoinColumn(name="id_plan", nullable=false)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name="id_persona", nullable=false)
    private Persona persona;


    public Subscripcion() {
    }

    public Subscripcion(Plan plan, Persona persona) {
        this.plan = plan;
        this.persona = persona;
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
