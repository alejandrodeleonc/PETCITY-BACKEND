package Services;

import Encapsulaciones.Permiso;

public class PermisoServices extends DBManage<Permiso> {
    private static PermisoServices instancia;

    private PermisoServices() { super(Permiso.class); }

    public static PermisoServices getInstancia(){
        if(instancia==null){
            instancia = new PermisoServices();
        }

        return instancia;
    }
}