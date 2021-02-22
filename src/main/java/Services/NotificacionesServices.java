package Services;


import Encapsulaciones.Factura;
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

    public boolean comprobarSiTieneNotificacionesDePagoHoy(Persona persona){

        EntityManager em = getEntityManager();
        String sql =         "SELECT * FROM NOTIFICACIONES n " +
                "JOIN PERSONA_NOTIFICACIONES pn ON (n.ID_NOTIFICACIONES = pn.NOTIFICACIONES_ID_NOTIFICACIONES) " +
                "AND (pn.PERSONA_ID_PERSONA ="+ persona.getId_persona() +") " +
                "WHERE (n.TIPO = 2) AND (CAST(n.FECHA_CREACION AS DATE) = CAST(GETDATE() AS DATE))";
        boolean status = false;
        List<Notificaciones> res = new ArrayList<Notificaciones>();


        try {
            res = em.createNativeQuery(sql, Notificaciones.class).getResultList();

            System.out.println("El tamano de las notificaciones => " + res.size());
            status = res.size() > 0 ? true : false;
        } finally {
            em.close();
        }

        return status;
    }

    public boolean comprobarSiEsDueno(Persona persona, Notificaciones noticacion){
        boolean status = false;
        EntityManager em = getEntityManager();
        String sql =         "SELECT * FROM PERSONA p \n" +
                "JOIN PERSONA_NOTIFICACIONES pn ON (p.ID_PERSONA = pn.PERSONA_ID_PERSONA)\n" +
                "AND (pn.NOTIFICACIONES_ID_NOTIFICACIONES = "+ noticacion.getId_notificaciones()+")";
        Persona res = null;
        try{
            res = (Persona) em.createNativeQuery(sql, Persona.class).getSingleResult();


            status = res  == null ? false :  res.getId_persona() == persona.getId_persona() ? true : false;



        }finally{
            em.close();
        }

        return status;
    }
}
