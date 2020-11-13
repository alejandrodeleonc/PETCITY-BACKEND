package Services;

import Encapsulaciones.Perro;
import Encapsulaciones.Persona;

public class PerroServices extends DBManage<Perro> {
    private static PerroServices instancia;

    private PerroServices() { super(Perro.class); }

    public static PerroServices getInstancia(){
        if(instancia==null){
            instancia = new PerroServices();
        }

        return instancia;
    }




    /**
     * METHODS FOR THIS CLASS
     */
}
