package Services;

import Encapsulaciones.Plan;

public class PerroVacunaServices  extends DBManage<PerroVacunaServices> {
    private static PerroVacunaServices instancia;

    private PerroVacunaServices() { super(PerroVacunaServices.class); }

    public static PerroVacunaServices getInstancia(){
        if(instancia==null){
            instancia = new PerroVacunaServices();
        }

        return instancia;
    }
}
