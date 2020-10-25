package Services;

import Encapsulaciones.Persona;
import Encapsulaciones.Plan;

public class PlanServices extends DBManage<Plan> {
    private static PlanServices instancia;

    private PlanServices() { super(Plan.class); }

    public static PlanServices getInstancia(){
        if(instancia==null){
            instancia = new PlanServices();
        }

        return instancia;
    }

    /**
     * METHODS FOR THIS CLASS
     */
}
