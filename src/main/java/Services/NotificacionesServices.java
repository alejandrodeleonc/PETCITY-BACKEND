package Services;


import Encapsulaciones.Notificaciones;
import Encapsulaciones.Perro;

public class NotificacionesServices extends DBManage<Notificaciones> {
    private static NotificacionesServices instancia;

    private NotificacionesServices() { super(Notificaciones.class); }

    public static NotificacionesServices getInstancia(){
        if(instancia==null){
            instancia = new NotificacionesServices();
        }

        return instancia;
    }
}
