package Controladores;

import Encapsulaciones.*;
import Services.*;
import com.google.gson.*;
import com.twilio.http.TwilioRestClient;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


import static Controladores.WebSocketControlador.*;
import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class MantenimientoControlador {
    private Javalin app;
    public static final String ACCOUNT_SID = "ACeaeeb50fe25fb5888ac39ca76cbd8315";
    public static final String AUTH_TOKEN = "803d7c4abe286f3bccd8e342d5caf55a";

    public MantenimientoControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {

        app.routes(() -> {
            path("/api/v1/mantenimiento", () -> {

                get("/informacion_persona", ctx -> {
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));

                    Map<String, Object> json = new HashMap();

                    json.put("persona", per);

                    ctx.status(200);
                    ctx.json(json);

                });

                post("/registrar_perro", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();



                    String id_perro = ctx.formParam("RFID_CODE");
                    String nombre = ctx.formParam("nombre");
                    Date fecha_registro = new Date();
                    int limite_repeticion_comida = 2;
                    Perro perro = new Perro(id_perro, nombre, fecha_registro, limite_repeticion_comida);


                    if (PerroServices.getInstancia().crear(perro)) {
                        res.put("msg", "Perro Registrado correctamente!");
                    } else {
                        res.put("msg", "Fallo la insercion en la base de datos!");
                        status = 401;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());
                });

                post("/subscribirme", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    Plan plan = PlanServices.getInstancia().find(Integer.valueOf(ctx.formParam("id_plan")));
                    Persona persona = PersonaServices.getInstancia().find(Integer.valueOf(ctx.formParam("id_persona")));
                    List<String> id_perros = ctx.formParams("perros");
                    List<Perro> perros = id_perros == null ? null : PerroServices.getInstancia().buscarVariosPerrosPorId(id_perros);

                    if (persona != null) {
                        if (plan != null) {
                            if (perros.size() == id_perros.size()) {
                                if (perros.size() < plan.getCantidad_maxima_de_perros()) {
                                    Subscripcion sub = new Subscripcion(plan, new Date(), perros);
                                    SubcripcionServices.getInstancia().crear(sub);
                                    persona.setSubcripciones(sub);
                                    PersonaServices.getInstancia().editar(persona);
                                    res.put("msg", "Se ha suscrito correctamente!");
                                } else {
                                    res.put("msg", "Necesita escoger un plan mas alto!");
                                    status = 401;
                                }
                            } else {
                                res.put("msg", "Parametros proporcionados incorrectos!");
                                status = 401;
                            }
                        } else {
                            res.put("msg", "El plan seleccionado no es correcto!");
                            status = 409;
                        }
                    } else {
                        res.put("msg", "La persona no existe!");
                        status = 409;
                    }

                    ctx.json(res.toMap());
                    ctx.status(status);

                });

                post("/subscribir/perros", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    List<String> id_perros = ctx.formParams("perros");
                    List<Perro> perros = id_perros == null ? null : PerroServices.getInstancia().buscarVariosPerrosPorId(id_perros);
                    Subscripcion sub = persona.getSubcripciones();

                    if (persona != null) {
                        if (perros != null) {
                            if (perros.size() == id_perros.size()) {
                                int restantes = sub.getPlan().getCantidad_maxima_de_perros() - sub.getPerros().size();
                                boolean aux = (restantes) > 0 ? true : false;
                                System.out.println("Perros restantes =>" + restantes);
                                if (aux && restantes <= sub.getPlan().getCantidad_maxima_de_perros()) {
                                    for (Perro perro : perros) {
                                        sub.addPerro(perro);
                                    }
                                    SubcripcionServices.getInstancia().editar(sub);
                                    if (perros.size() > 1) {
                                        res.put("msg", "Se han agregado los perros correctamente!");
                                    } else if (perros.size() == 1) {

                                        res.put("msg", "Se ha agregado el perro correctamente!");
                                    }
                                } else {
                                    res.put("msg", "No tiene espacio suficiente para agregar perros!");
                                    status = 409;
                                }

                            } else {
                                res.put("msg", "Parametros proporcionados incorrectos!");
                                status = 409;
                            }
                        } else {
                            res.put("msg", "No hay perros que anadir!");
                            status = 409;

                        }
                    } else {
                        res.put("msg", "La persona no existe!");
                        status = 401;

                    }


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

                post("/pagar", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));

                    if (per.getSubcripciones() != null) {
                        Subscripcion sub = per.getSubcripciones();
                        sub.setPago(true);
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MONTH, 3);
                        sub.setFechaVencimientoPago(cal.getTime());
                        per.setSubcripciones(sub);
                        SubcripcionServices.getInstancia().editar(sub);
                        PersonaServices.getInstancia().editar(per);
                        res.put("msg", "Su pago fue realizado con exito!");
                    } else {
                        status = 401;
                        res.put("msg", "Usted no tiene suscripciones");
                    }

                    ctx.status(status);
                    ctx.json(res);

                });

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
                    JSONObject res = new JSONObject();
                    int status = 200;

                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));

                    if (persona != null) {
                        ctx.json(persona.getSubcripciones().getPerros());

                    } else {
                        res.put("msg", "La persona no existe");
                        ctx.json(res.toMap());
                    }

                    ctx.status(status);
                });


//                post("/:id_perro/agregar_vacuna", ctx -> {
//                    int status = 200;
//                    JSONObject res = new JSONObject();
//                    Gson builder = new Gson();
////                    Perro perro = PerroServices.getInstancia().find(ctx.pathParam("id_perro"));
//                    Persona usuarioDeLaPeticion = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
//                    JSONObject body = new JSONObject(ctx.body().toString());
//
//                    System.out.println("Vacunas para domingo =>");
//                    List<Vacuna> lista = new ArrayList<Vacuna>();
//                    JsonObject root;
//
//                    DateFormat df = new SimpleDateFormat("DD/MM/yyyy");
//                    for (Object obj : body.getJSONArray("vacunas")) {
//                        root = new JsonParser().parse(obj.toString()).getAsJsonObject();
////                        String date = (String) root.get("fecha").value();
////                        System.out.println("Fecha =>" + df.format(date));
////                        System.out.println();
//                        Vacuna aux = new Vacuna(root.get("nombre").getAsString(), df.parse(root.get("fecha").getAsString()));
//                        lista.add(aux);
//                        VacunaServices.getInstancia().crear(aux);
//
//                    }
////                    perro.setVacunas(lista);
////                    PerroServices.getInstancia().editar(perro);
////                    System.out.println(lista);
////                    System.out.println(root.getAsJsonObject().get("ayer"));
//
//
//                });

                post("/:id_perro/comio", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.pathParam("id_perro");
                    String ip_dispensador = ctx.header("dispensador");
                    if (ip_dispensador != null) {
                        Dispensador dispensador = DispensadorServices.getInstancia().getDispensadorByDireccionIP(ip_dispensador);
                        Perro perro = PerroServices.getInstancia().buscarPerroByRFID(id_perro);

                        if (perro != null) {
                            Persona dueno = PerroServices.getInstancia().buscarDueno(perro);
                            if (perro.getPerdido()) {
                                Notificaciones not = new Notificaciones(perro.getNombre() + " estuvo en el dispensador  que esta en la " + dispensador.getDireccion() + ", Latitud: " + dispensador.getLatitud() + " Longitud: " + dispensador.getLongitud(), new Date(), 1);

                                dueno.addNotificacion(not);
                                NotificacionesServices.getInstancia().crear(not);
                                PersonaServices.getInstancia().editar(dueno);
//                                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
////                                https://www.google.com/maps?q=19.22111701965332,-70.52653503417969&z=17&hl=es
//                                Message message = Message.creator(
//                                        new com.twilio.type.PhoneNumber("whatsapp:+18296491998"),
//                                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
//                                        "https://www.google.com/maps?q=" + dispensador.getLatitud() + "," + dispensador.getLongitud() + "&z=17&hl=es\n" + not.getContenido())
//                                        .create();

//                                System.out.println(message.getSid());


                                List<UsuariosConectados> conexionesDelDueno = WebSocketControlador.buscarConexionesDeUsuarioConectadoByUser(dueno);
                                if (conexionesDelDueno.size() > 0) {
                                    System.out.println("Enviando  mensaje al dueno");
                                    enviarMensajeAunGrupo(conexionesDelDueno, not.getContenido());
                                }
                            }
                            boolean puede_comer = HistorialDeVisitasService.getInstancia().canEat(perro);
                            res.put("puede_comer", puede_comer);
//                        Perro perro, Dispensador dispensador, Date fecha, boolean is_comio
                            HistorialDeVisitas vista = new HistorialDeVisitas(perro, dispensador, new Date(), puede_comer);
                            HistorialDeVisitasService.getInstancia().crear(vista);
                        } else {
                            status = 409;
                            res.put("msg", "Error: Al buscar el dueño en la base de datos");

                        }

                    } else {
                        status = 401;
                        res.put("msg", "Error: No tiene origen");

                    }
                    ctx.status(status);
                    ctx.json(res.toMap());

                });

                post("/perro/:id/perdido", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    String header = ctx.header("Authorization");
                    int id = Integer.valueOf(ctx.pathParam("id"));

                    try{


                    Perro perro = PerroServices.getInstancia().find(id);

//                    enviarMensajeAClientesConectados("perro perdido", "rojo");

                    if (perro != null) {
                        if (!perro.getPerdido()) {

                            Persona dueno = PerroServices.getInstancia().buscarDueno(perro);
                            System.out.println("El dueno => " + dueno.getNombre());
                            if (dueno != null) {

                                if (FakeServices.getInstancia().compararHeaderUser(header, dueno)) {
                                    perro.setPerdido(true);
                                    PerroServices.getInstancia().editar(perro);
                                    res.put("msg", "El perro ha sido reportado como perdido!");
                                } else {
                                    res.put("msg", "No tiene permiso para modificar este perro");
                                    status = 401;
                                }
                            } else {
                                res.put("msg", "Este perro es callejero");
                            }
                        } else {
                            res.put("msg", "Este perro ya esta rerpotado como perdido");
                            status = 409;
                        }
                    } else {
                        status = 409;
                        res.put("msg", "El perro que busca no existe");
                    }
                    }catch(Exception e){
                        status = 409;
                        res.put("msg", "Datos invalidos");

                    }
                    ctx.status(status);
                    ctx.json(res.toMap());
                });

                post("/perro/:id/encontrado", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    String header = ctx.header("Authorization");
                    int id = Integer.valueOf(ctx.pathParam("id"));
                    Perro perro = PerroServices.getInstancia().find(id);

                    if (perro != null) {
                        if (perro.getPerdido()) {
                            Persona dueno = PerroServices.getInstancia().buscarDueno(perro);
                            System.out.println("El dueno => " + dueno.getNombre());
                            if (dueno != null) {
                                if (FakeServices.getInstancia().compararHeaderUser(header, dueno)) {
                                    perro.setPerdido(false);
                                    PerroServices.getInstancia().editar(perro);
                                    res.put("msg", "El perro ha sido encontrado");
                                } else {
                                    res.put("msg", "No tiene permiso para modificar este perro");
                                    status = 401;
                                }
                            } else {
                                res.put("msg", "Este perro es callejero");
                            }

                        } else {
                            res.put("msg", "Este perro no ha sido reportado como perdido");
                            status = 409;
                        }
                    } else {
                        status = 409;
                        res.put("msg", "El perro que busca no existe");
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());
                });

                get("/notificaciones_no_vistas", ctx -> {
                    Persona perso = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    if (perso != null) {
                        ctx.json(NotificacionesServices.getInstancia().getNotifciacionesNoVistas(perso));
                    } else {
                        ctx.status(401);
                        ctx.json(new JSONObject().put("msg", "No es un usuario valido"));
                    }

                });
                post("/notificaciones/:id_notificacion/visto", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    int id_notifacion = Integer.valueOf(ctx.pathParam("id_notificacion"));
                    Notificaciones notificacion = NotificacionesServices.getInstancia().find(id_notifacion);


                    if (notificacion != null) {

                        if (!notificacion.isEstado()) {
                            notificacion.setEstado(true);
                            NotificacionesServices.getInstancia().editar(notificacion);
                            res.put("msg", "Se ha visto la notificacion");
                        }else{
                            status = 200;
                            res.put("msg", "Ya se ha visto anteriormente");

                        }
                    } else {
                        res.put("msg", "La notifiacion no existe");
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());

                });




                post("/perros/:id_perro/agregar_vacuna", ctx->{
                    JSONObject res = new JSONObject();
                    int status = 200;
                    int id_perro = Integer.valueOf(ctx.pathParam("id_perro"));
                    JSONObject body = new JSONObject(ctx.body().toString());
                    Perro perro = PerroServices.getInstancia().find(id_perro);

                    if(perro != null){
                                System.out.println("Vacunas para domingo =>");
                                System.out.println(body);
                            if (body !=null) {

                                JsonObject root;

                                DateFormat df = new SimpleDateFormat("DD/MM/yyyy");
                                for (Object obj : body.getJSONArray("vacunas")) {
                                    System.out.println(obj);
                                    root = new JsonParser().parse(obj.toString()).getAsJsonObject();
                                    Vacuna aux = VacunaServices.getInstancia().find(root.get("id_vacuna").getAsInt());
                                    if(aux != null){
                                        PerroVacuna vacuna = new PerroVacuna(df.parse(root.get("fecha").getAsString()), aux);
                                        perro.addVacuna(vacuna);
                                    }
                                }
                                PerroServices.getInstancia().editar(perro);
                                }
                    }else{
                        res.put("msg", "El perro no existe");
                        status = 409;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());

                });


                get("/vacunas", ctx ->{
                    ctx.json(VacunaServices.getInstancia().findAll());
                });




            });
        });
    }


}