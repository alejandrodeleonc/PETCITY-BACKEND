package Services;

import Encapsulaciones.Perro;
import Encapsulaciones.Persona;

import java.util.ArrayList;
import java.util.List;

public class PerroServices extends DBManage<Perro> {
    private static PerroServices instancia;

    private PerroServices() { super(Perro.class); }

    public static PerroServices getInstancia(){
        if(instancia==null){
            instancia = new PerroServices();
        }

        return instancia;
    }


    public List<Perro> buscarVariosPerrosPorId(List<String> id_perros){
        List<Perro> perros= new ArrayList<Perro>();
        Perro aux;
        for(String id : id_perros){
            aux = PerroServices.getInstancia().find(id);
            if(aux != null){
            perros.add(aux);
            }
        }

        return perros;
    }



    /**
     * METHODS FOR THIS CLASS
     */
}
