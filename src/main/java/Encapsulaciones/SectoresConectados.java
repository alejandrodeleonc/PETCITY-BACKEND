package Encapsulaciones;

import org.eclipse.jetty.websocket.api.Session;

public class SectoresConectados {
    private Sector sector;
    private Session sesion;

    public SectoresConectados(Sector sector, Session sesion) {
        this.sector = sector;
        this.sesion = sesion;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Session getSesion() {
        return sesion;
    }

    public void setSesion(Session sesion) {
        this.sesion = sesion;
    }
}
