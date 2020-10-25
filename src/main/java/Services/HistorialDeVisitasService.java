package Services;



import Encapsulaciones.HistorialDeVisitas;
import Encapsulaciones.Perro;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialDeVisitasService extends DBManage<HistorialDeVisitas>{
    private static HistorialDeVisitasService instancia;

    private HistorialDeVisitasService() { super(HistorialDeVisitas.class); }

    public static HistorialDeVisitasService getInstancia(){
        if(instancia==null){
            instancia = new HistorialDeVisitasService();
        }

        return instancia;
    }

    public List<HistorialDeVisitas> getHistorialByPerroId(String id_perro)  throws PersistenceException {
        EntityManager em = getEntityManager();
        List<HistorialDeVisitas> historial;
        try{
            historial = em.createQuery("SELECT h FROM HISTORIAL_DE_VISITAS h WHERE h.id_perro LIKE :id_perro")
                          .setParameter("id_perro", id_perro)
                          .getResultList();
        } finally {
            em.close();
        }

        return historial;
    }


    public boolean canEat(Perro perro){
        boolean aux = true;
        List<HistorialDeVisitas> historial = this.getHistorialByPerroId(perro.getId_perro());
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int today =  localDate.getDayOfMonth();
        int i = 0;
        for(HistorialDeVisitas reg : historial){
            if(reg.getFecha().getDay() == today){
                i++;
            }
        }

        if(i>perro.getLimite_repeticion_comida()){
            aux = false;
            i = 0;
        }

        return aux;
    }
}

