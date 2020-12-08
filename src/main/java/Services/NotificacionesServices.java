package Services;


import Encapsulaciones.Notificaciones;
import Encapsulaciones.Perro;
import Encapsulaciones.Persona;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class NotificacionesServices extends DBManage<Notificaciones> {
    private static NotificacionesServices instancia;

    private NotificacionesServices() { super(Notificaciones.class); }

    public static NotificacionesServices getInstancia(){
        if(instancia==null){
            instancia = new NotificacionesServices();
        }

        return instancia;
    }


    public List<Notificaciones> getNotifciacionesNoVistas(Persona persona){
        List<Notificaciones> notificaciones = new ArrayList<Notificaciones>();

        EntityManager em = getEntityManager();
        List<Notificaciones> res = new ArrayList<Notificaciones>();
        try{
            res = em.createNativeQuery("SELECT * FROM NOTIFICACIONES N\n" +
                    "INNER JOIN PERSONA_NOTIFICACIONES pn  ON (PN.NOTIFICACIONES_ID_NOTIFICACIONES = n.ID_NOTIFICACIONES)  \n" +
                    "AND PN.PERSONA_ID_PERSONA = "+persona.getId_persona() +" AND N.ESTADO = FALSE ", Notificaciones.class).getResultList();

            if(res != null){
                notificaciones = res;
            }
        } finally {
            em.close();
        }

        return notificaciones;
    }
    public List<Notificaciones> getNotifciacionesNoVistasPaginadas(Persona persona, int cantidad){
        List<Notificaciones> notificaciones = new ArrayList<Notificaciones>();

        return notificaciones;
    }
}
