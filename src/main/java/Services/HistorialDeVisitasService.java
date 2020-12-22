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

    public List<HistorialDeVisitas> getHistorialByPerroId(Perro perro)  throws PersistenceException {
        EntityManager em = getEntityManager();
        List<HistorialDeVisitas> historial = new ArrayList<HistorialDeVisitas>();
        try{
            historial = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_VISITAS where ID_PERRO = " + perro.getId_perro() , HistorialDeVisitas.class).getResultList();

        } finally {
            em.close();
        }

        return historial;
    }
    public List<HistorialDeVisitas> getHistorialByPerroIdAndDate(String id_perro, Date fecha)  throws PersistenceException {
        EntityManager em = getEntityManager();
        List<HistorialDeVisitas> historial;
        try{
            historial = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_VISITAS where (ID_PERRO = '" + id_perro + "') and " +
                    "(IS_COMIO = true) and (CAST(FECHA AS DATE) = CAST(GETDATE() AS DATE))", HistorialDeVisitas.class).getResultList();

        } finally {
            em.close();
        }

        return historial;
    }




    public boolean canEat(Perro perro){
        boolean aux = true;
        EntityManager em = getEntityManager();
        List<HistorialDeVisitas> historial;
        try{
            historial = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_VISITAS where (ID_PERRO = '" + perro.getId_perro()+ "') and " +
                    "(IS_COMIO = true) and (CAST(FECHA AS DATE) = CAST(GETDATE() AS DATE))", HistorialDeVisitas.class).getResultList();

        } finally {
            em.close();
        }
        System.out.println("Tamano =>" + historial.size());
        for (HistorialDeVisitas his : historial) {
        System.out.println("Historial => " + his.getFecha());
        }

        if(historial.size()>=perro.getLimite_repeticion_comida()){
            aux = false;
        }


        return aux;
    }
}

