package Services;

import Encapsulaciones.Factura;
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

            res = (Persona)em.createNativeQuery("SELECT * FROM PERSONA p\n" +
                    "INNER JOIN SUBSCRIPCION_PERRO sp ON (p.SUBCRIPCIONES_ID_SUBSCRIPCION = sp.SUBSCRIPCION_ID_SUBSCRIPCION) AND (sp.PERROS_ID_PERRO = "+perro.getId_perro() +")", Persona.class).getSingleResult();




            return res;
        } finally {
            em.close();
        }
    }

    public int cantidadDePerrosSinDueno(){
        EntityManager em = getEntityManager();
        int res = 0;
        try{
            List<Perro> perro = em.createNativeQuery("SELECT * FROM PERRO p \n" +
                    "INNER JOIN SUBSCRIPCION_PERRO sp  ON (p.ID_PERRO = sp.PERROS_ID_PERRO) AND (sp.SUBSCRIPCION_ID_SUBSCRIPCION= pe.SUBCRIPCIONES_ID_SUBSCRIPCION)\n" +
                    "INNER JOIN PERSONA pe ON (pe.USUARIO = 'admin') " , Perro.class).getResultList();

            res = perro.size();
            System.out.println("Perros => " + perro.size());

        } finally {
            em.close();
        }
        return res;
    }

    public List<Perro> getPerrosParaDonacion(){
        EntityManager em = getEntityManager();
        List<Perro> perros = new ArrayList<Perro>();
        try{
             perros = em.createNativeQuery("SELECT * FROM PERRO p \n" +
                     "INNER JOIN SUBSCRIPCION_PERRO sp  ON (p.ID_PERRO = sp.PERROS_ID_PERRO) AND (sp.SUBSCRIPCION_ID_SUBSCRIPCION= pe.SUBCRIPCIONES_ID_SUBSCRIPCION)\n" +
                     "INNER JOIN PERSONA pe ON (pe.USUARIO IN ('admin', 'pi'))", Perro.class).getResultList();
        } finally {
            em.close();
        }
        return perros;
    }

    public boolean agregarCambiosAPerro(Perro perroEnBD, Perro perroConCambio){
        boolean status = false;



        return status;
    }


    /**
     * METHODS FOR THIS CLASS
     */
}
