package Services;

import Encapsulaciones.Foto;

public class FotoServices extends DBManage<Foto> {
    private static FotoServices instancia;

    private FotoServices() { super(Foto.class); }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }

        return instancia;
    }
}