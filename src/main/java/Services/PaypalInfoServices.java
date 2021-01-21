package Services;

import Encapsulaciones.PaypalInfo;

public class PaypalInfoServices extends DBManage<PaypalInfo> {
private static PaypalInfoServices instancia;

private PaypalInfoServices() { super(PaypalInfo.class); }

public static PaypalInfoServices getInstancia(){
        if(instancia==null){
        instancia = new PaypalInfoServices();
        }

        return instancia;
        }
}
