package Encapsulaciones;

import org.eclipse.jetty.websocket.api.Session;

public class UsuariosConectados {

    private String usuario;
    private Session sesion;


    public UsuariosConectados(String usuario, Session sesion) {
        this.usuario = usuario;
        this.sesion = sesion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Session getSesion() {
        return sesion;
    }

    public void setSesion(Session sesion) {
        this.sesion = sesion;
    }
}
