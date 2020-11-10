package Services;


import Encapsulaciones.*;

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


    public List<Perro> getPerrosOfAnUser(Persona user) {
        List<Perro> res =  new ArrayList<Perro>();

        if (user.getSubcripciones().size() > 0) {
            List<Subscripcion> subs =  user.getSubcripciones();
            for(Subscripcion sub : subs){
                SubscripcionPerro aux = SubscripcionPerroServices.getInstancia().getIdSubcripcionPerroBySubcripcionID(sub.getId_subscripcion());

                res.add(aux.getPerro());
            }


        }
        return res;
    }
}
