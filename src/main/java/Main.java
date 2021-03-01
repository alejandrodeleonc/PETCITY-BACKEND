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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {


    public static void main(String[] args) {

        //JAVALIN CONFIG
        Javalin app = Javalin.create(config -> {
//            config.addStaticFiles("/public"); //STATIC FILES -> /resources/public
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
            config.requestCacheSize = new Long(9999999);
            config.addStaticFiles("/public");
        });

        
        app.start(8000);
        DBStart.getInstancia().init();
        InitializeBDService.getInstancia().intialize();

        new ApiControlador(app).aplicarRutas();
        /*
        new MantenimientoControlador(app).aplicarRutas();
        new WebSocketControlador(app).aplicarRutas();
        new AutenticacionControlador(app).aplicarRutas();*/
    }
}