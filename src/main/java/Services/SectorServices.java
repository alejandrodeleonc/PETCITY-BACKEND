package Services;

import Encapsulaciones.Dispensador;
import Encapsulaciones.Sector;

import java.util.List;

public class SectorServices  extends DBManage<Sector> {
    private static SectorServices instancia;

    private SectorServices() { super(Sector.class); }

    public static SectorServices getInstancia(){
        if(instancia==null){
            instancia = new SectorServices();
        }

        return instancia;
    }

    public Sector getSectorAlquePertenece(Dispensador dispensador){
        Sector sectorPerteneciente = null;
        List<Sector> sectores = this.findAll();

        if(sectores.size() > 0){
            System.out.println("1");
            double lat2 = Double.valueOf(dispensador.getLatitud());
            double long2 = Double.valueOf(dispensador.getLongitud());



            for(Sector sector : sectores){
                double lat1 = Double.valueOf(sector.getLatitud());
                double long1 = Double.valueOf(sector.getLongitud());
                System.out.println("\nDispensador -> "+dispensador.getId_dispensador() + ", " + sector.getNombre());
                System.out.format("Distancia -> %f", calcularDistancia(lat1, lat2, long1, long2));
                double distancia = calcularDistancia(lat1, lat2, long1, long2);


                if(distancia <= sector.getRadio()){
                    sectorPerteneciente = sector;
                    System.out.println("2");
                    break;
                }

            }
        }

        return sectorPerteneciente;
    }

    private double calcularDistancia(double lat1, double lat2, double long1, double long2){

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        long1 = Math.toRadians(long1);
        long2 = Math.toRadians(long2);
        double deltaLat =  lat2 - lat1;
        double deltaLong =  long2 - long1;
        double radioTierra = 6372.795477598;
        double a =  (Math.pow(Math.sin(deltaLat / 2), 2) + ( (Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLong / 2),2))));

        double c = (2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a) ));


        return  ((c * radioTierra) * 1000)*1.005 ;

    }
}
