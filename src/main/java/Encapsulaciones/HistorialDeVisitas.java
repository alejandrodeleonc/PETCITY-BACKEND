package Encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name="HISTORIAL_DE_VISITAS")
@javax.persistence.Entity(name = "HISTORIAL_DE_VISITAS")
public class HistorialDeVisitas implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_historialdevisitas;

    @ManyToOne
    @JoinColumn(name="id_perro", nullable=false)
    private Perro perro;


    @ManyToOne
    @JoinColumn(name="id_dispensador", nullable=false)
    private Dispensador dispensador;

    private Date fecha;
    private boolean is_comio;

    public HistorialDeVisitas() {
    }

    public HistorialDeVisitas(Perro perro, Dispensador dispensador, Date fecha, boolean is_comio) {
        this.perro = perro;
        this.dispensador = dispensador;
        this.fecha = fecha;
        this.is_comio = is_comio;
    }

    public Perro getPerro() {
        return perro;
    }

    public void setPerro(Perro perro) {
        this.perro = perro;
    }

    public Dispensador getDispensador() {
        return dispensador;
    }

    public void setDispensador(Dispensador dispensador) {
        this.dispensador = dispensador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isIs_comio() {
        return is_comio;
    }

    public void setIs_comio(boolean is_comio) {
        this.is_comio = is_comio;
    }
}
