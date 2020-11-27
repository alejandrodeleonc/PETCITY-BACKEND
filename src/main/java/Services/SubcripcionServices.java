package Services;


import Encapsulaciones.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class SubcripcionServices extends DBManage<Subscripcion> {
    private static SubcripcionServices instancia;

    private SubcripcionServices() { super(Subscripcion.class); }

    public static SubcripcionServices getInstancia(){
        if(instancia==null){
            instancia = new SubcripcionServices();
        }

        return instancia;
    }


//    public List<Perro> getPerrosOfAnUser(Persona user) {
//        EntityManager em = getEntityManager();
//        List<SubscripcionPerro> res = new ArrayList<SubscripcionPerro>();
//        List<Perro> perros = new ArrayList<Perro>();
//
//        try{
//            for(Subscripcion sub :  user.getSubcripciones() ){
//            res = em.createNativeQuery("SELECT * FROM SUBCRIPCION_PERRO where SUBSCRIPCION_ID_SUBSCRIPCION = " + sub.getId_subscripcion(), SubscripcionPerro.class).getResultList();
//
//            for(SubscripcionPerro subper : res ){
//                perros.add(subper.getPerro());
//            }
//
//            }
//
//        } finally {
//            em.close();
//        }
//        return perros;
//    }


}
