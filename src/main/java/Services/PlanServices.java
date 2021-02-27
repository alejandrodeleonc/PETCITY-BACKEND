package Services;

import Encapsulaciones.Persona;
import Encapsulaciones.Plan;

import javax.persistence.EntityManager;

public class PlanServices extends DBManage<Plan> {
    private static PlanServices instancia;

    private PlanServices() { super(Plan.class); }

    public static PlanServices getInstancia(){
        if(instancia==null){
            instancia = new PlanServices();
        }

        return instancia;
    }


    public Plan getMaxPlan() {
//        SELECT * FROM plan p ORDER BY  (p.CANTIDAD_MAXIMA_DE_PERROS) DESC LIMIT 1

        EntityManager em = getEntityManager();
        Plan res = null;
        try {

            res = (Plan) em.createNativeQuery("SELECT * FROM plan p ORDER BY  (p.CANTIDAD_MAXIMA_DE_PERROS) DESC LIMIT 1", Plan.class).getSingleResult();

            return res;
        } catch (Exception e) {
            return res = null;
        } finally {
            em.close();
        }
    }

    /**
     * METHODS FOR THIS CLASS
     */
}
