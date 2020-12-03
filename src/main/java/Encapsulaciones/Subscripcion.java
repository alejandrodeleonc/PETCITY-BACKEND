package Encapsulaciones;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SUBSCRIPCION")
public class Subscripcion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_subscripcion;
    private boolean pago;
    private float monto;
    @OneToOne
    @JoinColumn(name = "id_plan")
    private Plan plan;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Perro> perros;


//    @ManyToOne
//    @JoinColumn(name="id_persona", nullable=false)
//    private Persona persona;

    private Date fechaVencimientoPago;


    public Subscripcion() {
    }

    //    public Subscripcion(Plan plan, Date fechaVencimientoPago) {
//        this.plan = plan;
//        this.perros = new ArrayList<Perro>();
//        this.fechaVencimientoPago = fechaVencimientoPago;
//    }
    public Subscripcion(Plan plan, Date fechaVencimientoPago, List<Perro> perros) {
        this.plan = plan;
        this.perros = perros == null ? new ArrayList<Perro>() : perros;
        this.fechaVencimientoPago = fechaVencimientoPago;
    }

    public int getId_subscripcion() {
        return id_subscripcion;
    }

    public void setId_subscripcion(int id_subscripcion) {
        this.id_subscripcion = id_subscripcion;
    }

    public boolean addPerro(Perro perro) {
        boolean aux = false;

        if (perros.size() < plan.getCantidad_maxima_de_perros()) {
            aux = true;
            perros.add(perro);
        }

        return aux;
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


    public List<Perro> getPerros() {
        return perros;
    }

    public void setPerros(List<Perro> perros) {
        this.perros = perros;
    }

    public Date getFechaVencimientoPago() {
        return fechaVencimientoPago;
    }

    public void setFechaVencimientoPago(Date fechaVencimientoPago) {
        this.fechaVencimientoPago = fechaVencimientoPago;
    }
}
