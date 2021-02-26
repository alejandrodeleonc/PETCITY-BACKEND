package Services;

import Encapsulaciones.Rol;


public class RolServices extends DBManage<Rol> {
    private static RolServices instancia;

    private RolServices() { super(Rol.class); }

    public static RolServices getInstancia(){
        if(instancia==null){
            instancia = new RolServices();
        }

        return instancia;
    }
}
