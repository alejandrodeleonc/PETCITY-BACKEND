package Services;

import Encapsulaciones.Accion;

public class AccionServices extends DBManage<Accion> {
    private static AccionServices instancia;

    private AccionServices() { super(Accion.class); }

    public static AccionServices getInstancia(){
        if(instancia==null){
            instancia = new AccionServices();
        }

        return instancia;
    }
}
