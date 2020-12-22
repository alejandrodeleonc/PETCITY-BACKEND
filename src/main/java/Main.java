import Controladores.ApiControlador;
import Controladores.AutenticacionControlador;
import Controladores.MantenimientoControlador;
import Controladores.WebSocketControlador;
import Encapsulaciones.*;
import io.javalin.Javalin;
import Services.*;

import java.text.Format;

import java.text.SimpleDateFormat;

import io.javalin.core.util.RouteOverviewPlugin;

import static j2html.TagCreator.a;

import java.util.Date;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {


    public static void main(String[] args) {

        //JAVALIN CONFIG
        Javalin app = Javalin.create(config -> {
//            config.addStaticFiles("/public"); //STATIC FILES -> /resources/public
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
        });
        app.start(8000);
        DBStart.getInstancia().init();
        Persona persoo = new Persona("Alejandro", "4021527", new Date(), "j amor",
                "admin", "admin", "sdfsasdf");
        Persona pi = new Persona("Raspberry Pi", "000000", new Date(), "La zursa",
                "pi", "raspberry", "ninguno");
        PersonaServices.getInstancia().editar(persoo);
        PersonaServices.getInstancia().editar(pi);

//        Format forma = new SimpleDateFormat("dd/MM/yyyy");
        Perro pers = new Perro("484849535648495350491310","Billy", new Date(), 2) ;
//        data:image/jpeg;base64,
//        pers.setFoto(new Foto("foto de : "+ pers.getNombre(),"base64" ,
        PerroServices.getInstancia().crear(pers);
//
        Plan pl = new Plan("Prueba", Float.parseFloat("870.89") , 3, 3);
        PlanServices.getInstancia().crear(pl);

        Dispensador dispen = new Dispensador("0013A20040A4D103", "-70.5289368", "9.2209351","Calle 7");
        DispensadorServices.getInstancia().crear(dispen);
//        HistorialDeVisitas vis = new HistorialDeVisitas(pers, dispen, new Date() , true);
//        HistorialDeVisitasService.getInstancia().crear(vis);

        new ApiControlador(app).aplicarRutas();
        /*
        new MantenimientoControlador(app).aplicarRutas();
        new WebSocketControlador(app).aplicarRutas();
        new AutenticacionControlador(app).aplicarRutas();*/
    }
}