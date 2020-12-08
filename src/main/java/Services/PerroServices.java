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
            aux = PerroServices.getInstancia().find(id);
            if (aux != null) {
                perros.add(aux);
            }
        }

        return perros;
    }

    public Persona buscarDueno(Perro perro) {

        EntityManager em = getEntityManager();
        List<Persona> res = new ArrayList<Persona>();
        try {
            res = em.createNativeQuery("SELECT * FROM PERSONA p\n" +
                    "INNER JOIN SUBSCRIPCION s ON s.ID_SUBSCRIPCION = p.SUBCRIPCIONES_ID_SUBSCRIPCION \n" +
                    "INNER JOIN SUBSCRIPCION_PERRO sp ON sp.PERROS_ID_PERRO = '" + perro.getId_perro() + "'", Persona.class).getResultList();

            return res.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * METHODS FOR THIS CLASS
     */
}
