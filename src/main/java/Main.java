import Controladores.Autenticacion;
import Controladores.Mantenimiento;
import Controladores.WebSocket;
import Encapsulaciones.*;
import com.oracle.xmlns.webservices.jaxws_databinding.JavaWsdlMappingType;
import io.javalin.Javalin;
import Services.*;
import io.javalin.core.util.Header;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.sse.SseClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.security.Key;
import java.lang.reflect.Field;
import java.text.Format;
import java.util.*;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.sse.SseClient;
import org.eclipse.jetty.websocket.api.Session;

import static j2html.TagCreator.*;
import static j2html.TagCreator.a;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import static io.javalin.apibuilder.ApiBuilder.*;
import static java.lang.Integer.*;

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
//        Persona persoo = new Persona("Alejandro", "4021527", new Date(), "j amor",
//                "admin", "admin", "sdfsasdf");
//        Persona pi = new Persona("Raspberry Pi", "000000", new Date(), "La zursa",
//                "pi", "raspberry", "ninguno");
//        PersonaServices.getInstancia().editar(persoo);
//        PersonaServices.getInstancia().editar(pi);
//
//        Format forma = new SimpleDateFormat("dd/MM/yyyy");
//        Perro pers = new Perro("484849535648495350491310","Billy", forma.format(new Date()), 2) ;
//        PerroServices.getInstancia().crear(pers);
//
//        Plan pl = new Plan("Prueba", Float.parseFloat("870.89") , 3, 3);
//        PlanServices.getInstancia().crear(pl);
//
//        Dispensador dispen = new Dispensador("0013A20040A4D103", "1234", "1234","Calle 7");
//        DispensadorServices.getInstancia().crear(dispen);
//        HistorialDeVisitas vis = new HistorialDeVisitas(pers, dispen, new Date() , true);
//        HistorialDeVisitasService.getInstancia().crear(vis);

//        app.options("/*", ctx -> {
//
//            System.out.println("Entrando al metodo de options");
//            String accessControlRequestHeaders = ctx.header("Access-Control-Request-Headers");
//            if (accessControlRequestHeaders != null) {
//                ctx.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
//            }
//
//            String accessControlRequestMethod = ctx.header("Access-Control-Request-Method");
//            if (accessControlRequestMethod != null) {
//                ctx.header("Access-Control-Allow-Methods", accessControlRequestMethod);
//            }
//
//            ctx.status(200).result("OK");
//
//        });

//            app.before("/*", ctx ->{
//                ctx.header("Access-Control-Allow-Origin", "*");
//            });


        app.routes(() -> {

            after("/*", ctx -> {
                ctx.header("Access-Control-Allow-Origin", "*");
                ctx.header("Access-Control-Allow-Methods", "*");
                ctx.header("Access-Control-Allow-Headers", "*");
//                ctx.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
//                ctx.header("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Authorization,Cookie");
            });


            path("/api/v1", () -> {
                before("/mantenimiento/*", ctx -> {
                    String header = "Authorization";
                    String prefijo = "Bearer";

                    String headerAutentificacion = ctx.header(header);
                    System.out.println("El header");
                    System.out.println(ctx.headerMap());
                    String accessControlRequestHeaders = ctx.header("Access-Control-Request-Headers");

                    if (headerAutentificacion == null || !headerAutentificacion.startsWith(prefijo)) {
                        System.out.println("No hay");
//                        throw new ForbiddenResponse("No tiene permiso para acceder al recurso");
                    }
                });

            });
        });
        new Mantenimiento(app).aplicarRutas();
        new WebSocket(app).aplicarRutas();
        new Autenticacion(app).aplicarRutas();
    }
}