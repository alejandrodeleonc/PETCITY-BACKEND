package Encapsulaciones;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @OneToOne
    @NotNull
    private PaypalInfo paypal;

    public Factura() {
    }

    public Factura(Persona persona,Date fecha, Subscripcion subscripcion,  PaypalInfo paypal) {
        this.persona = persona;
        this.fecha = fecha;
        this.subscripcion = subscripcion;
        this.monto = paypal.getValue();
        this.paypal = paypal;
    }

    public int getPersona() {
        return persona.getId_persona();
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

    public Map<String, Object> getSubscripcion() {
        Map<String, Object> json = new HashMap();
        json.put("suscripcion_id", subscripcion.getId_subscripcion());
        json.put("perros", subscripcion.getPerros().size());
        json.put("id_plan", subscripcion.getPlan().getId_plan());
        json.put("meses_del_plan", subscripcion.getPlan().getMeses_actividad());

        return json;
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
