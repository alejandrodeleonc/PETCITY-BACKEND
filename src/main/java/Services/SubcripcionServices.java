package Services;


import Encapsulaciones.Plan;
import Encapsulaciones.Subscripcion;

public class SubcripcionServices extends DBManage<Subscripcion> {
    private static SubcripcionServices instancia;

    private SubcripcionServices() { super(Subscripcion.class); }

    public static SubcripcionServices getInstancia(){
        if(instancia==null){
            instancia = new SubcripcionServices();
        }

        return instancia;
    }

}
