package Controladores;

import Encapsulaciones.*;
import Services.*;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import kong.unirest.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;
import static java.lang.Integer.parseInt;

public class Mantenimiento {
    private Javalin app;

    public Mantenimiento(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {

        app.routes(() -> {
            path("/api/v1/mantenimiento", () -> {

                post("/registrar_perro", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("RFID_CODE");
                    String nombre = ctx.formParam("nombre");
                    Date fecha_registro = new Date();
                    int limite_repeticion_comida = 2;
                    Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Perro perro = new Perro(id_perro, nombre, formatter.format(fecha_registro), limite_repeticion_comida);

                    if (PerroServices.getInstancia().crear(perro)) {
                        res.put("msg", "Perro Registrado correctamente!");
                    } else {
                        res.put("msg", "Fallo la insercion en la base de datos!");
                        status = 401;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());
                });

//                post("/subscribirme", ctx -> {
//                    int status = 200;
//                    JSONObject res = new JSONObject();
//                    Plan plan = PlanServices.getInstancia().find(Integer.valueOf(ctx.formParam("plan")));
//                    Persona person = PersonaServices.getInstancia().find(Integer.valueOf(ctx.formParam("persona")));
//
//                    Calendar cal = Calendar.getInstance();
//                    cal.add(Calendar.MONTH, plan.getMeses_actividad());
////                    Subscripcion subscripcion = new Subscripcion(plan, person, cal.getTime());
//                    Subscripcion subscripcion = new Subscripcion(plan, cal.getTime());
//                    if (SubcripcionServices.getInstancia().crear(subscripcion)) {
//                        person.addSubscripcion(subscripcion);
//                        PersonaServices.getInstancia().editar(person);
//                        res.put("resultado", "Susbcripcion correcta!");
//                    } else {
//                        res.put("msg", "Fallo la insercion en la base de datos!");
//                        status = 401;
//                    }
//                    ctx.status(status);
//                    ctx.json(res.toMap());
//
//                });

                post("/subscribir/:id_persona/perros", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    int id = ctx.pathParam("id_persona", Integer.class).get();
                    Subscripcion sub = SubcripcionServices.getInstancia().find(id);
                    Map<String, List<String>> id_perros = ctx.formParamMap();
                    System.out.println("Arreglo =>");
                    System.out.println(id_perros.get("perro"));





                    ctx.status(status);
                    ctx.json(res.toMap());

                });
                post("/vacunas/:id", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    String id = ctx.pathParam("id");

                    Perro perro = PerroServices.getInstancia().find(id);

                    if (perro != null) {

                    } else {
                        status = 409;
                    }

                    ctx.status(status);
                });

//                post("/:id/pagar", ctx -> {
//                    JSONObject res = new JSONObject();
//                    int status = 200;
//                    String id_sub = ctx.pathParam("id");
//                     Persona  per = getUserFromHeader(ctx.header("Authorization"));
//                     if(per.getSubcripciones().size()>0){
//                     Subscripcion sub = SubcripcionServices.getInstancia().find(id_sub);
//                        if(sub != null){
//                            if(sub.getId_persona() == per.getId_persona() || per.getUsuario().equalsIgnoreCase("admin")){
//                                sub.setPago(true);
//                                SubcripcionServices.getInstancia().editar(sub);
//                                res.put("msg", "Su pago fue realizado con exito!");
//                            }else{
//                                status = 401;
//                                res.put("msg", "Usted no puede realizar el pago");
//                            }
//                        }else{
//                            status = 409;
//                            res.put("msg", "La suscripcion no existe");
//                        }
//
//                     }else{
//                         status = 401;
//                         res.put("msg", "Usted no tiene suscripciones");
//                     }
//
//                     ctx.status(status);
//                     ctx.json(res);
//
//                });

                post("/visita", ctx -> {
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("perro");
                    Perro perro = PerroServices.getInstancia().find(id_perro);
                    if (ctx.header("FROM") != null) {
                        int id_dispensador = Integer.valueOf(ctx.header("FROM"));
                        System.out.println("El id =>" + id_dispensador);
                        Dispensador dispensador = DispensadorServices.getInstancia().find(id_dispensador);
                        Date fecha = new Date();
                        boolean is_comio = Boolean.valueOf(ctx.formParam("is_comio"));

                        HistorialDeVisitas visita = new HistorialDeVisitas(perro, dispensador, fecha, is_comio);
                        HistorialDeVisitasService.getInstancia().crear(visita);
                        res.put("msg", "Perro Registrado correctamente!");
                        ctx.json(res.toMap());
                    } else {
                        throw new ForbiddenResponse("Falto el dispensador de origen");
                    }

                });


                get("/perros", ctx -> {
                    Map<String, Object> res = new HashMap<>();

                    List<Perro> perros = PerroServices.getInstancia().findAll();

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(perros);
                    System.out.println(jsonString);
                    res.put("perros", perros);
                    ctx.json(res);
                });

//                get("/usuarios/:user/perros", ctx -> {
//                    String id = ctx.pathParam("user");
//                    Map<String, Object> res = new HashMap<>();
//                    Persona person = PersonaServices.getInstancia().findByUser(id);
//                    System.out.print("Persona => " + person.getNombre() + "\n");
//                    List<Perro> perros = SubcripcionServices.getInstancia().getPerrosOfAnUser(person);
//                    Gson gson = new Gson();
//                    String jsonString = gson.toJson(perros);
//                    System.out.println("El arreglo => " + jsonString);
//
//                    res.put("perros", perros);
//                    ctx.json(res);
//                });

//                post("/:id_perro/comio", ctx -> {
//                    int status = 200;
//                    JSONObject res = new JSONObject();
//                    String id_perro = ctx.pathParam("id_perro");
//                    String ip_dispensador = ctx.header("dispensador");
//                    if (ip_dispensador != null) {
//                        Dispensador dispensador = DispensadorServices.getInstancia().getDispensadorByDireccionIP(ip_dispensador);
//                        Perro perro = PerroServices.getInstancia().find(id_perro);
//                        if (perro != null) {
//                            if (perro.getPerdido()) {
//                                Persona dueno = SubscripcionPerroServices.getInstancia().getPersonaByPerro(perro.getId_perro());
//                                Notificaciones not = new Notificaciones(dueno, perro.getNombre() + " estuvo en el dispensador  que esta en la " + dispensador.getDireccion() + ", Latitud: "+ dispensador.getLatitud() + " Longitud: "+ dispensador.getLongitud() , new Date());
//                                NotificacionesServices.getInstancia().crear(not);
//
//                                List<UsuariosConectados> conexionesDelDueno = buscarConexionesDeUsuarioConectadoByUser(dueno);
//                                if (conexionesDelDueno.size() > 0) {
//                                    System.out.println("Enviando  mensaje al dueno");
//                                    enviarMensajeAunGrupo(conexionesDelDueno, not.getContenido());
//                                }
//                            }
//                            boolean puede_comer = HistorialDeVisitasService.getInstancia().canEat(perro);
//                            res.put("puede_comer", puede_comer);
////                        Perro perro, Dispensador dispensador, Date fecha, boolean is_comio
//                            HistorialDeVisitas vista = new HistorialDeVisitas(perro, dispensador, new Date(), puede_comer);
//                            HistorialDeVisitasService.getInstancia().crear(vista);
//                        } else {
//                            status = 409;
//                            res.put("msg", "Error: Al buscar el dueño en la base de datos");
//
//                        }
//
//                    } else {
//                        status = 401;
//                        res.put("msg", "Error: No tiene origen");
//
//                    }
//                    ctx.status(status);
//                    ctx.json(res.toMap());
//                });

//                post("/perro/:id/perdido", ctx -> {
//                    JSONObject res = new JSONObject();
//                    int status = 200;
//                    String header = ctx.header("Authorization");
//                    String id = ctx.pathParam("id");
//                    Perro perro = PerroServices.getInstancia().find(id);
//
////                    enviarMensajeAClientesConectados("perro perdido", "rojo");
//                    if (perro != null) {
//                        if (!perro.getPerdido()) {
//                            Persona dueno = SubscripcionPerroServices.getInstancia().getPersonaByPerro(perro.getId_perro());
//                            System.out.println("El dueno => " + dueno.getNombre());
//                            if (dueno != null) {
//
//                                if (compararHeaderUser(header, dueno)) {
//                                    perro.setPerdido(true);
//                                    PerroServices.getInstancia().editar(perro);
//                                    res.put("msg", "El perro ha sido reportado como perdido!");
//                                } else {
//                                    res.put("msg", "No tiene permiso para modificar este perro");
//                                    status = 401;
//                                }
//                            } else {
//                                res.put("msg", "Este perro es callejero");
//                            }
//                        } else {
//                            res.put("msg", "Este perro ya esta rerpotado como perdido");
//                            status = 409;
//                        }
//                    } else {
//                        status = 409;
//                        res.put("msg", "El perro que busca no existe");
//                    }
//
//                    ctx.status(status);
//                    ctx.json(res.toMap());
//                });

//                post("/perro/:id/encontrado", ctx -> {
//                    JSONObject res = new JSONObject();
//                    int status = 200;
//                    String header = ctx.header("Authorization");
//                    String id = ctx.pathParam("id");
//                    Perro perro = PerroServices.getInstancia().find(id);
//
//                    if (perro != null) {
//                        if (perro.getPerdido()) {
//                            Persona dueno = SubscripcionPerroServices.getInstancia().getPersonaByPerro(perro.getId_perro());
//                            System.out.println("El dueno => " + dueno.getNombre());
//                            if (dueno != null) {
//
//                                if (compararHeaderUser(header, dueno)) {
//                                    perro.setPerdido(false);
//                                    PerroServices.getInstancia().editar(perro);
//                                    res.put("msg", "El perro ha sido encontrado");
//                                } else {
//                                    res.put("msg", "No tiene permiso para modificar este perro");
//                                    status = 401;
//                                }
//                            } else {
//                                res.put("msg", "Este perro es callejero");
//                            }
//
//                        } else {
//                            res.put("msg", "Este perro no ha sido reportado como perdido");
//                            status = 409;
//                        }
//                    } else {
//                        status = 409;
//                        res.put("msg", "El perro que busca no existe");
//                    }
//
//                    ctx.status(status);
//                    ctx.json(res.toMap());
//                });

//        get("/notificaciones_no_vistas", ctx -> {
//            Persona perso = getUserFromHeader(ctx.header("Authorization"));
//
//            ctx.json(NotificacionesServices.getInstancia().getNotifciacionesNoVistas(perso));
//            System.out.println("Persona desde noti=>" + perso.getNombre());
//
//        });

                before("/administracion/*", ctx -> {
                    System.out.println("Aqui es donde se manejaran los permisos");
                });


                post("/administracion/crear_dispensador", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    String id_dispensador = ctx.formParam("dispensador");
                    String longitud = ctx.formParam("longitud");
                    String latitud = ctx.formParam("latitud");
                    String direccion = ctx.formParam("direccion");

                    Dispensador dispensador = new Dispensador(id_dispensador, longitud, latitud, direccion);


                    if (DispensadorServices.getInstancia().crear(dispensador)) {
                        res.put("resultado", "Dispensador creado correctamente!");
                    } else {
                        status = 401;
                        res.put("msg", "Fallo la insercion en la base de datos!");
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());
                });

                post("/administracion/crear_plan", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    String nombre = ctx.formParam("nombre");
                    float costo = Float.parseFloat(ctx.formParam("costo"));
                    int meses_actividad = parseInt(ctx.formParam("meses_actividad"));
                    int cantidad_maxima_de_perros = parseInt(ctx.formParam("cantidad_maxima_de_perros"));


                    Plan plan = new Plan(nombre, costo, meses_actividad, cantidad_maxima_de_perros);

                    if (PlanServices.getInstancia().crear(plan)) {
                        res.put("resultado", "Plan creado correctamente!");
                    } else {
                        status = 401;
                        res.put("msg", "Fallo la insercion en la base de datos!");
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());
                });
            });
        });
    }


}
