package Services;

import Encapsulaciones.Dispensador;
import Encapsulaciones.Persona;
import Encapsulaciones.Sector;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class SectorServices extends DBManage<Sector> {
    private static SectorServices instancia;

    private SectorServices() {
        super(Sector.class);
        this.padres.add(new JsonParser().parse("{'tabla':'DISPENSADOR', 'colum': 'SECTOR_ID_SECTOR'}"));

    }

    public static SectorServices getInstancia() {
        if (instancia == null) {
            instancia = new SectorServices();
        }

        return instancia;
    }

    public Sector getSectorAlquePertenece(Object obj) {
        Sector sectorPerteneciente = null;
        List<Sector> sectores = this.findAll();

        if (sectores.size() > 0) {
            System.out.println("1");
            double lat2 = Double.valueOf(obj instanceof Dispensador ? ((Dispensador) obj).getLatitud() : ((Sector) obj).getLatitud());
            double long2 = Double.valueOf(obj instanceof Dispensador ? ((Dispensador) obj).getLongitud() : ((Sector) obj).getLongitud());


            for (Sector sector : sectores) {

                double lat1 = Double.valueOf(sector.getLatitud());
                double long1 = Double.valueOf(sector.getLongitud());
                double distancia = calcularDistancia(lat1, lat2, long1, long2);

                boolean solapa = distancia <= sector.getRadio();
                if (!solapa && obj instanceof Sector) {
//                    if(distancia +  ((Sector) obj).getRadio() +  <= sector.getRadio()){
//
//                    }
                }

                if (solapa) {
                    sectorPerteneciente = sector;
                }


            }
        }

        return sectorPerteneciente;
    }

    private double calcularDistancia(double lat1, double lat2, double long1, double long2) {

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        long1 = Math.toRadians(long1);
        long2 = Math.toRadians(long2);
        double deltaLat = lat2 - lat1;
        double deltaLong = long2 - long1;
        double radioTierra = 6372.795477598;
        double a = (Math.pow(Math.sin(deltaLat / 2), 2) + ((Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLong / 2), 2))));

        double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));


        return ((c * radioTierra) * 1000) * 1.005;

    }


    public String getRandomColor() {
        String[] letters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String color = "#";
        String[] grises = {"#b5b6b7", "#7f7f7f", "#888888", "#929292", "#9c9c9c", "#a6a6a6", "#b0b0b0", "#bababa", "#9c9c9c", "#acacac", "#bcbcbc", "#cccccc", "#dddddd", "#eeeeee", "#ffffff", "#9c9c9c", "#808080", "#656565"};


        for (int i = 0; i < 6; i++) {
            color += letters[(int) Math.floor(Math.random() * 16)];
        }

        if (this.colorEstaEnUso(color) || Arrays.stream(grises).anyMatch("s"::equalsIgnoreCase)) {
            color = this.getRandomColor();
        }
        return color;
    }

    public boolean colorEstaEnUso(String color) {
        boolean status = false;
        EntityManager em = getEntityManager();
        String sql = "SELECT CASE WHEN (SELECT COUNT(*) FROM SECTOR  WHERE (COLOR = '" + color + "') LIMIT 1) > 0 THEN TRUE ELSE FALSE END AS RESULT";
        try {
            status = (Boolean) em.createNativeQuery(sql).getSingleResult();

        } finally {
            em.close();
        }

        return status;
    }

    public auxNotificacion calculoSolape(Sector new_sector) {
        EntityManager em = getEntityManager();
//        System.out.println("***********")
        auxNotificacion sectorSolapado = new auxNotificacion();
        List<Sector> sectores = SectorServices.getInstancia().findAll();
        boolean puntosUnicos= SectorServices.getInstancia().findBy("LATITUD", new_sector.getLatitud()) == null && SectorServices.getInstancia().findBy("LONGITUD", new_sector.getLongitud()) == null  ? true :false;
        if(  puntosUnicos) {


            if (new_sector.getRadio() < 201 && new_sector.getRadio() > 0) {
                System.out.println("Entro el if de 75");
                if (sectores != null) {


                    for (Sector sector : sectores) {
                        String sql = "SELECT hs.ID_SECTOR, \n" +
                                "111.111 * DEGREES(ACOS(COS(RADIANS(" + new_sector.getLatitud() + ")) * COS(RADIANS(cast(hs.LATITUD AS decimal(30,20)) )) * COS(RADIANS(" + new_sector.getLongitud() + " - cast(hs.LONGITUD AS decimal(30,20)) )) + SIN(RADIANS(" + new_sector.getLatitud() + ")) * SIN(RADIANS(cast(hs.LATITUD AS decimal(30,20))))))*1000 AS distancia_en_metros, hs.RADIO, hs.NOMBRE\n" +
                                "FROM SECTOR  hs \n" +
                                "WHERE (111.111 * DEGREES(ACOS(COS(RADIANS(" + new_sector.getLatitud() + ")) * COS(RADIANS(cast(hs.LATITUD AS decimal(30,20)) )) * COS(RADIANS(" + new_sector.getLongitud() + " - cast(hs.LONGITUD AS decimal(30,20)) )) + SIN(RADIANS(" + new_sector.getLatitud() + ")) * SIN(RADIANS(cast(hs.LATITUD AS decimal(30,20))))))*1000) ORDER BY hs.RADIO ASC;";


                        try {
                            //                    List<Object> res =  em.createNativeQuery(sql).getResultList();
                            //                    List<Object> res =  em.
                            Query query = em.createNativeQuery(sql);
                            List<Object[]> results = query.getResultList();
                            for (Object obj : results) {
                                Object[] fields = (Object[]) obj;

                                Double distancia_completa = Double.valueOf(fields[1].toString());
                                Double sumaRadios = (new_sector.getRadio() + Double.valueOf(fields[2].toString()));
                                Double distancia_real = sumaRadios - distancia_completa;
                                System.out.println("\t\tFields => " + fields[0].toString() + " / " + fields[1].toString() + " // " + fields[2].toString() + " - " + fields[3].toString());
                                System.out.println("___________________________________________________________________");
                                System.out.println("Sector Actual =>" + sector.getNombre() + " - " + sector.getRadio());
                                System.out.println("Sector nuevo =>" + new_sector.getNombre() + " - " + new_sector.getRadio());
                                System.out.println("\t\tSector a comparar => " + fields[3].toString() + " - Radio => " + fields[2].toString());
                                System.out.format("Suma => %f\n", sumaRadios);
                                System.out.format("distanciaReal => %f\n", distancia_real);
                                System.out.println("___________________________________________________________________");
                                System.out.println();

//                                || sumaRadios <= distancia_real
//                            Double auxRadioNuevo = new_sector.getRadio() < sector.getRadio()  && distancia_real < sector.getRadio() ? Double.valueOf( distancia_real + sector.getRadio()) : new_sector.getRadio() + sector.getRadio();
                                System.out.println("sumaRadios" + sumaRadios);
                                System.out.println("distancia_completa" + distancia_completa);
                                System.out.println("distancia_real" + distancia_real);
                                System.out.println("NEGATIVE");
//                                if(
//                                        Integer.valueOf(distancia_real.toString()) > -1 ? true : false
//
//                                ){
                                System.out.println("**************************************");
                                System.out.println("Antes entrar al if del break");
                                System.out.println("**************************************");
                                if (!(distancia_real.compareTo(0.00) <= 0)) {
//                                    sectorSolapado.entrySet("sector",parser.parse(SectorServices.getInstancia().find(Integer.valueOf(fields[0].toString()))));
                                    sectorSolapado.setData(sector);
                                    sectorSolapado.setDetalle("Solapa con el sector: " + sector.getNombre());
                                    System.out.println(sectorSolapado.getDetalle());
                                    sectorSolapado.setError(true);
                                    break;
                                } else {
                                    sectorSolapado.setError(false);
                                }


                            }
                        } catch (Exception e) {

                        } finally {
                            em.close();
                        }
                    }

                }
            } else {
                sectorSolapado.setDetalle("Problemas con el radio (Máx 200mts)");
            }
        }else{

                sectorSolapado.setDetalle("No se puede crear un sector dentro de otro");
        }
        return sectorSolapado;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public class auxNotificacion {
//        "{'error':true, msg:'Accion no permitida', 'detalle':'', 'sector':null}"
        boolean error = true;
        String msg = "Accion no permitida";
        String detalle;
        Object data;

        public auxNotificacion() {
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getDetalle() {
            return detalle;
        }

        public void setDetalle(String detalle) {
            this.detalle = detalle;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}


