package Controladores;

import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ApiControlador {
    private Javalin app;

    public ApiControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {


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

                before("/administracion/*", ctx -> {

                });

            });
        });


        new MantenimientoControlador(app).aplicarRutas();
        new WebSocketControlador(app).aplicarRutas();
        new AutenticacionControlador(app).aplicarRutas();
        new AdministracionControlador(app).aplicarRutas();


    }
}
