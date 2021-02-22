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
    private Date fechaDeAdquisicion;

    private boolean status;


    public Subscripcion() {
    }

    //    public Subscripcion(Plan plan, Date fechaVencimientoPago) {
//        this.plan = plan;
//        this.perros = new ArrayList<Perro>();
//        this.fechaVencimientoPago = fechaVencimientoPago;
//    }
    public Subscripcion(Plan plan, Date fechaVencimientoPago, Date fechaDeAdquisicion, List<Perro> perros) {
        this.plan = plan;
        this.perros = perros == null ? new ArrayList<Perro>() : perros;
        this.fechaVencimientoPago = fechaVencimientoPago;
        this.fechaDeAdquisicion = fechaDeAdquisicion;
        this.status = true;
    }

    public int getId_subscripcion() {
        return id_subscripcion;
    }

    public void setId_subscripcion(int id_subscripcion) {
        this.id_subscripcion = id_subscripcion;
    }

    public boolean addPerro(Perro perro) {
        boolean aux = false;

//        if (perros.size() < plan.getCantidad_maxima_de_perros()) {
//            aux = true;
//            perros.add(perro);
//        }

            aux = true;
            perros.add(perro);


        return aux;
    }

    public void addPerros(List<Perro> perros){
        for(Perro perro : perros){
            this.perros.add(perro);
        }

    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
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


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

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

    public Date getFechaDeAdquisicion() {
        return fechaDeAdquisicion;
    }

    public void setFechaDeAdquisicion(Date fechaDeAdquisicion) {
        this.fechaDeAdquisicion = fechaDeAdquisicion;
    }

    public boolean  borrarPerro(Perro perro){
        boolean aux = false;
        int i = 0;
        for(Perro dog : this.perros){
            if(dog.getId_perro() == perro.getId_perro()){
                this.perros.remove(i);
                aux = true;
                break;
            }
            i++;
        }
        return aux;
    }

    public int cuposDePerrosRestantes(){
        int aux =  this.getPlan().getCantidad_maxima_de_perros() - this.perros.size() < 0 ? 0:this.getPlan().getCantidad_maxima_de_perros() - this.perros.size();
        return aux;
    }
}
