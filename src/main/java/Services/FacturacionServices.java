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
        List<Factura> facts = new ArrayList<Factura>();
        try{
            facts = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_FACTURACION hdf WHERE hdf.PERSONA_ID_PERSONA = "+ persona.getId_persona()+" ORDER BY hdf.FECHA DESC LIMIT 1", Factura.class).getResultList();
            res  = facts.size() >0 ? facts.get(0):null;
        } finally {
            em.close();
        }

        return res;

    }
    public List<Factura> getHistorialDeFacturacion(Persona persona){
        Dispensador dispensador = null;
        EntityManager em = getEntityManager();
        List<Factura> res = new ArrayList<Factura>();
        try{
            res = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_FACTURACION hdf WHERE hdf.PERSONA_ID_PERSONA = "+ persona.getId_persona()+" ORDER BY hdf.FECHA DESC", Factura.class).getResultList();

        } finally {
            em.close();
        }

        return res;
    }

 }
