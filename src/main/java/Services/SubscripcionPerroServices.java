package Services;


import Encapsulaciones.HistorialDeVisitas;
import Encapsulaciones.Perro;
import Encapsulaciones.Persona;
import Encapsulaciones.SubscripcionPerro;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class SubscripcionPerroServices extends DBManage<SubscripcionPerro> {
    private static SubscripcionPerroServices instancia;

    private SubscripcionPerroServices() { super(SubscripcionPerro.class); }

    public static SubscripcionPerroServices getInstancia(){
        if(instancia==null){
            instancia = new SubscripcionPerroServices();
        }

        return instancia;
    }

    public SubscripcionPerro getIdSubcripcionPerroBySubcripcionID( int id_suscripcion){
        EntityManager em = getEntityManager();
        List<SubscripcionPerro> res = new ArrayList<SubscripcionPerro>();

        try{
            res = em.createNativeQuery("SELECT * FROM SUBCRIPCION_PERRO where SUBSCRIPCION_ID_SUBSCRIPCION = " + id_suscripcion, SubscripcionPerro.class).getResultList();

        } finally {
            em.close();
        }

        return res.get(0);
    }

    public Persona getPersonaByPerro(String id_perro){
        Persona persona = null;
        EntityManager em = getEntityManager();
        List<SubscripcionPerro> res = new ArrayList<SubscripcionPerro>();
        try{
            res = em.createNativeQuery("SELECT * FROM SUBCRIPCION_PERRO where PERRO_ID_PERRO = " + id_perro, SubscripcionPerro.class).getResultList();

            if(res != null){
               persona = res.get(0).getSubscripcion().getPersona();
            }
        } finally {
            em.close();
        }

        return persona;
    }
}
