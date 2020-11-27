package Services;





import Encapsulaciones.Dispensador;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DispensadorServices extends DBManage<Dispensador>{
    private static DispensadorServices instancia;

    private DispensadorServices() { super(Dispensador.class); }

    public static DispensadorServices getInstancia(){
        if(instancia==null){
            instancia = new DispensadorServices();
        }

        return instancia;
    }

    public Dispensador getDispensadorByDireccionIP(String ip){
        Dispensador dispensador = null;
        EntityManager em = getEntityManager();
        List<Dispensador> res = new ArrayList<Dispensador>();
        try{
            res = em.createNativeQuery("SELECT * FROM DISPENSADOR where DISPENSADOR = '" + ip +"'", Dispensador.class).getResultList();

            if(res != null){
                dispensador = res.get(0);
            }
        } finally {
            em.close();
        }

        return dispensador;
    }


    /**
     * METHODS FOR THIS CLASS
     */
}


