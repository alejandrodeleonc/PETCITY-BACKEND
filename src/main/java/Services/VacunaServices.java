package Services;


import Encapsulaciones.Vacuna;

public class VacunaServices extends DBManage<Vacuna> {
    private static VacunaServices instancia;

    private VacunaServices() { super(Vacuna.class); }

    public static VacunaServices getInstancia(){
        if(instancia==null){
            instancia = new VacunaServices();
        }

        return instancia;
    }
}
