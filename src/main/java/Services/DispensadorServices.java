package Services;





import Encapsulaciones.Dispensador;

public class DispensadorServices extends DBManage<Dispensador>{
    private static DispensadorServices instancia;

    private DispensadorServices() { super(Dispensador.class); }

    public static DispensadorServices getInstancia(){
        if(instancia==null){
            instancia = new DispensadorServices();
        }

        return instancia;
    }

    /**
     * METHODS FOR THIS CLASS
     */
}


