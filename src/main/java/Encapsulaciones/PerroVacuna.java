package Encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VACUNACION")
public class PerroVacuna implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_vacunacion;

    private Date fecha;

    @ManyToOne
    private Perro perro;

    @ManyToOne
    private Vacuna vacuna;

    public PerroVacuna() {
    }

    public PerroVacuna(Perro perro, Date fecha, Vacuna vacuna) {
        this.fecha = fecha;
        this.vacuna = vacuna;
    }

    public int getId_vacunacion() {
        return id_vacunacion;
    }

    public void setId_vacunacion(int id_vacunacion) {
        this.id_vacunacion = id_vacunacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }
}
