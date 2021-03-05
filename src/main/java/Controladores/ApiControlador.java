package Controladores;

import Encapsulaciones.Accion;
import Encapsulaciones.PermisosyAcciones;
import Encapsulaciones.Persona;
import Encapsulaciones.Rol;
import Services.FakeServices;
import Services.RolServices;
import io.javalin.Javalin;
import kong.unirest.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;

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


//        app.get("/:name", ctx -> {
////            File localFile = new File(FakeServices.getInstancia().getRutaProyecto() + "/src/main/resources/public" + "/" +ctx.pathParam("name"));
////            InputStream inputStream = new BufferedInputStream(new FileInputStream(localFile));
////            ctx.header("Content-Disposition", "attachment; filename=\"" + localFile.getName() + "\"");
////            ctx.header("Content-Length", String.valueOf(localFile.length()));
////            ctx.result(inputStream);
//        });
//        webSocketSectores
        app.config.accessManager((handler, ctx, permittedAccion) -> {
            if(ctx.path().contains("auth") || ctx.path().contains("rutas") ||  ctx.path().contains("webSocketServidor")   ||ctx.path().contains("mensajeServidor") || ctx.path().contains("roles") || ctx.path().contains("foto") ||  ctx.path().contains("webSocketServidor")){
                handler.handle(ctx);
            }else {
                final Persona usuario = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));

                System.out.println("permittedAccion -> ");
                FakeServices.RolApp rr = permittedAccion.toArray().length > 0 ? (FakeServices.RolApp) permittedAccion.toArray()[0] : null;
                Boolean is_accept;
                if (rr != null) {
                    rr.getAccion();
                    System.out.println(rr.accion);
                    is_accept = rr.accion != null && usuario != null;
                } else {
                    is_accept = false;
                }

                System.out.println(ctx.path().contains("auth"));
                if (is_accept) {
                    is_accept = false;
                    for (Rol rol : usuario.getPersonasroles()) {
                        System.out.println("PASO 2:");
                        for (Accion accion : rol.getAcciones()) {
                            System.out.println("PASO 3:");
                            if (accion.getId_accion() == rr.accion.getId_accion()) {
                                System.out.println("PASO 4:");
                                System.out.println(String.format("El Usuario: %s - con el Rol: %s tiene permiso", usuario.getUsuario(), rol.getNombre()));
                                is_accept = true;
                                break;
                            }
                        }
                    }
                }

                if (is_accept) {
                    handler.handle(ctx);
                } else {
                    JSONObject res = new JSONObject();
                    res.put("msg", "No tienes permiso para acceder...");
                    System.out.println("No tiene permiso para acceder..");
                    ctx.json(res.toMap());
                    ctx.status(401).result("No tiene permiso para acceder...");
                    return;
                }
                System.out.println("PASO 5:");
            }
        });

        app.routes(() -> {

            after("/*", ctx -> {
                ctx.header("Access-Control-Allow-Origin", "*");
                ctx.header("Access-Control-Allow-Methods", "*");
                ctx.header("Access-Control-Allow-Headers", "*");
//                ctx.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
//                ctx.header("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Authorization,Cookie");
            });


            path("/api/v1", () -> {
            before("/*", ctx->{
                System.out.println("Ruta -> ");
//                System.out.println(ctx.);


            });
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
