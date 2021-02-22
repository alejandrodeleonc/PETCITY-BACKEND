package Services;

import Encapsulaciones.Dispensador;
import Encapsulaciones.Factura;
import Encapsulaciones.Persona;
import Encapsulaciones.Plan;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class FacturacionServices extends DBManage<Factura> {
    private static FacturacionServices instancia;

    private FacturacionServices() {
        super(Factura.class);
    }

    public static FacturacionServices getInstancia() {
        if (instancia == null) {
            instancia = new FacturacionServices();
        }

        return instancia;
    }

    public Factura getUltimaFacturaByPersona(Persona persona) {
        Dispensador dispensador = null;
        EntityManager em = getEntityManager();
        Factura res = null;
        List<Factura> facts = new ArrayList<Factura>();
        try {
            facts = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_FACTURACION hdf WHERE hdf.PERSONA_ID_PERSONA = " + persona.getId_persona() + " ORDER BY hdf.FECHA DESC LIMIT 1", Factura.class).getResultList();
            res = facts.size() > 0 ? facts.get(0) : null;
        } finally {
            em.close();
        }

        return res;

    }

    public List<Factura> getHistorialDeFacturacion(Persona persona) {
        Dispensador dispensador = null;
        EntityManager em = getEntityManager();
        List<Factura> res = new ArrayList<Factura>();
        try {
            res = em.createNativeQuery("SELECT * FROM HISTORIAL_DE_FACTURACION hdf WHERE hdf.PERSONA_ID_PERSONA = " + persona.getId_persona() + " ORDER BY hdf.FECHA DESC", Factura.class).getResultList();

        } finally {
            em.close();
        }

        return res;
    }

    public float getGanancias() {
        EntityManager em = getEntityManager();
        Float res = 0.0f;
        try {
            List<Factura> facturas = FacturacionServices.getInstancia().findAll();

            if (facturas.size() > 0) {
                for (Factura factura : facturas) {
                    res += factura.getMonto();
                }
            }

        } finally {
            em.close();
        }

        return res;
    }

    public Map<Integer, Object> getGananciasMensuales() {
        EntityManager em = getEntityManager();
        Map<Integer, Object> res = new HashMap();
        String sql = "SELECT MONTH(FECHA), SUM(MONTO) FROM HISTORIAL_DE_FACTURACION hdf WHERE YEAR(FECHA) = 2021 GROUP BY MONTH (FECHA)";
        Query query = em.createNativeQuery(sql);
        try {

            for (int i = 0; i < 12; i++) {
                res.put(i + 1, 0.0);
            }

            List<Object[]> results = query.getResultList();
            for (Object obj : results) {
                Object[] fields = (Object[]) obj;
                res.put(Integer.valueOf(fields[0].toString()), fields[1]);
            }


        } finally {
            em.close();
        }

        return new TreeMap<>(res);
    }


    public boolean puedeCambiarDePlan(Plan plan, Persona per) {
        EntityManager em = getEntityManager();
        String sql = "SELECT * FROM HISTORIAL_DE_FACTURACION hdf WHERE (MONTO = " + plan.getCosto() + ") " +
                "AND (PERSONA_ID_PERSONA =" + per.getId_persona() + ") ORDER BY (FECHA) DESC LIMIT 1";
        boolean status = false;
        List<Factura> res = new ArrayList<Factura>();


        try {
            res = em.createNativeQuery(sql).getResultList();

            status = res.size() > 0 ? true : false;
        } finally {
            em.close();
        }

        return status;
    }


}
