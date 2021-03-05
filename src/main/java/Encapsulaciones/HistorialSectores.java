package Encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HISTORIAL_DE_SECTORES")
public class HistorialSectores implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_historiadesectores;

    private String ip;
    private Date date;
    private String estado;

    public HistorialSectores() {
    }

    public HistorialSectores(String ip, Date date) {
        this.ip = ip;
        this.date = date;
        this.estado = "";
    }

    public int getId_historiadesectores() {
        return id_historiadesectores;
    }

    public void setId_historiadesectores(int id_historiadesectores) {
        this.id_historiadesectores = id_historiadesectores;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
