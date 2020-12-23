package Services;

import Encapsulaciones.Dispensador;
import Encapsulaciones.Factura;
import Encapsulaciones.Persona;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class FacturacionServices extends DBManage<Factura> {
private static FacturacionServices instancia;

private FacturacionServices(){super(Factura.class);}

    public static FacturacionServices getInstancia(){
        if(instancia==null){
        instancia=new FacturacionServices();
        }

        return instancia;
        }

    public Factura getUltimaFacturaByPersona(Persona persona){
        Dispensador dispensador = null;
        EntityManager em = getEntityManager();
        Factura res = null;
        try{
            res = (Factura)em.createNativeQuery("SELECT * FROM HISTORIAL_DE_FACTURACION hdf WHERE hdf.PERSONA_ID_PERSONA = "+ persona.getId_persona()+" ORDER BY hdf.FECHA DESC LIMIT 1", Factura.class).getSingleResult();

        } finally {
            em.close();
        }

        return res;

    }

 }
