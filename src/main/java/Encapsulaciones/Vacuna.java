package Encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="VACUNA")
public class Vacuna implements Serializable {
    @Id
    private int id_vacuna;

    private String nombreVacuna;
    private Date fechaQueSePuso;

    @ManyToOne
    @JoinColumn(name="id_plan", nullable=false)
    private Perro perro_vacuna;
    public Vacuna() {
    }

    public Vacuna(String nombreVacuna, Date fechaQueSePuso) {
        this.nombreVacuna = nombreVacuna;
        this.fechaQueSePuso = fechaQueSePuso;
    }

    public int getId_vacuna() {
        return id_vacuna;
    }

    public void setId_vacuna(int id_vacuna) {
        this.id_vacuna = id_vacuna;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public Date getFechaQueSePuso() {
        return fechaQueSePuso;
    }

    public void setFechaQueSePuso(Date fechaQueSePuso) {
        this.fechaQueSePuso = fechaQueSePuso;
    }
}
