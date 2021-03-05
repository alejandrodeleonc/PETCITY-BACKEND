package Services;

import Encapsulaciones.HistorialSectores;

public class HistorialSectoresServices extends DBManage<HistorialSectores> {
    private static HistorialSectoresServices instancia;

    private HistorialSectoresServices() { super(HistorialSectores.class); }

    public static HistorialSectoresServices getInstancia(){
        if(instancia==null){
            instancia = new HistorialSectoresServices();
        }

        return instancia;
    }
}