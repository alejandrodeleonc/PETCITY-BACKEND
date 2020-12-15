package Services;

import Encapsulaciones.Notificaciones;
import Encapsulaciones.Perro;
import Encapsulaciones.Persona;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class PerroServices extends DBManage<Perro> {
    private static PerroServices instancia;

    private PerroServices() {
        super(Perro.class);
    }

    public static PerroServices getInstancia() {
        if (instancia == null) {
            instancia = new PerroServices();
        }

        return instancia;
    }


    public List<Perro> buscarVariosPerrosPorId(List<String> id_perros) {
        List<Perro> perros = new ArrayList<Perro>();
        Perro aux;
        for (String id : id_perros) {
            aux = PerroServices.getInstancia().find(Integer.valueOf(id));
            if (aux != null) {
                perros.add(aux);
            }
        }

        return perros;
    }

    public Perro buscarPerroByRFID(String rfid){
        EntityManager em = getEntityManager();
        try {


            return (Perro) em.createNativeQuery("SELECT * FROM PERRO WHERE RFID_CODE = '"+ rfid+ "'\n", Perro.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public Persona buscarDueno(Perro perro) {

        EntityManager em = getEntityManager();
        Persona res = null;
        try {

            res = (Persona) em.createNativeQuery("SELECT * FROM PERSONA p\n" +
                    "INNER JOIN SUBSCRIPCION s ON s.ID_SUBSCRIPCION = p.SUBCRIPCIONES_ID_SUBSCRIPCION \n" +
                    "INNER JOIN SUBSCRIPCION_PERRO sp ON sp.PERROS_ID_PERRO = " +perro.getId_perro() , Persona.class).getSingleResult();

            System.out.println("Buscando el duenño");
            System.out.println(res);



            return res;
        } finally {
            em.close();
        }
    }

    /**
     * METHODS FOR THIS CLASS
     */
}
