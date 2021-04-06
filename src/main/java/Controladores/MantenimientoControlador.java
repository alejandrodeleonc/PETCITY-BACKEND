package Controladores;

import Encapsulaciones.*;
import Services.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.twilio.http.TwilioRestClient;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.text.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


import static Controladores.WebSocketControlador.*;
import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.core.security.SecurityUtil.roles;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
//import org.h2.engine.Role;
import io.javalin.core.security.Role;

import javax.swing.filechooser.FileSystemView;

public class MantenimientoControlador {
    private Javalin app;
    public static final String ACCOUNT_SID = "ACeaeeb50fe25fb5888ac39ca76cbd8315";
    public static final String AUTH_TOKEN = "0ebf7a8ccad907727f9339ec8a43affe";

    public MantenimientoControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {


        app.routes(() -> {
            path("/api/v1/mantenimiento", () -> {

                get("/roles", ctx -> {

                    List<Rol> roles = RolServices.getInstancia().findAll();
                    if (roles.size() > 0) {
                        ctx.json(roles);
                    } else {
                        ctx.result("No hay roles");
                    }
                });


                get("/informacion_persona", ctx -> {
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));

                    Map<String, Object> json = new HashMap();


                    System.out.println("El nombre =>" + per.getUsuario());
                    System.out.println(!(per.getUsuario().equalsIgnoreCase("admin") || per.getUsuario().equalsIgnoreCase("pi")));

                    if (!(per.getUsuario().equalsIgnoreCase("admin") || per.getUsuario().equalsIgnoreCase("pi"))) {
                        if (per.getSubcripciones() != null) {
                            boolean pago = FakeServices.getInstancia().verificarSielPagoEstaAlDia(per);
                            boolean notificaciones = NotificacionesServices.getInstancia().comprobarSiTieneNotificacionesDePagoHoy(per);

                            if (!pago && !notificaciones) {
                                Notificaciones not = new Notificaciones("Su pago se encuentra en atraso", new Date(), 2);
                                NotificacionesServices.getInstancia().crear(not);

                                per.addNotificacion(not);
                                PersonaServices.getInstancia().editar(per);
                                FakeServices.getInstancia().enviarCorreoByPersona(per, "Notificacion de atraso de pago", "" +
                                        "Un cordial saludo " + per.getNombre() + ", este correo es un recordatorio amigable de su su plan con pet city esta" +
                                        "atrasado en el pago, favor pagar lo antes posible ");
                            }
                        }
                    }
                    json.put("persona", per);


                    ctx.status(200);
                    ctx.json(json);

                }, Collections.singleton(new FakeServices.RolApp("persona", "ver")));

                get("/informacion_pago", ctx -> {
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    List<Factura> facturas = FacturacionServices.getInstancia().getHistorialDeFacturacion(per);
                    int status = 200;
                    Map<String, Object> json = new HashMap();
                    if (per.getSubcripciones() != null) {
//                        FakeServices.getInstancia().verificarSielPagoEstaAlDia(per);

//                        if (!FakeServices.getInstancia().verificarSielPagoEstaAlDia(per)) {
//                            Notificaciones not = new Notificaciones("Su pago se encuentra en atraso", new Date(), 2);
//                            NotificacionesServices.getInstancia().crear(not);
//
//                            per.addNotificacion(not);
//                            PersonaServices.getInstancia().editar(per);
//                            FakeServices.getInstancia().enviarCorreoByPersona(per, "Notificacion de atraso de pago", "" +
//                                    "Un cordial saludo " + per.getNombre() + ", este correo es un recordatorio amigable de su su plan con pet city esta" +
//                                    "atrasado en el pago, favor pagar lo antes posible ");
//                        }
                        json.put("pago_al_dia", FakeServices.getInstancia().verificarSielPagoEstaAlDia(per));
                        json.put("monto", per.getSubcripciones().getPlan().getCosto());
                        json.put("pagar_antes", per.getSubcripciones().getFechaVencimientoPago());
                    } else {
                        json.put("msg", "No tiene suscripcion");
                        status = 409;
                    }
                    ctx.status(status);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("factura", "ver")));

                get("/historial_facturacion", ctx -> {
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    List<Factura> facturas = FacturacionServices.getInstancia().getHistorialDeFacturacion(per);
                    Map<String, Object> json = new HashMap();

                    json.put("historial_de_facturacion", facturas);

                    ctx.status(200);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("factura", "ver")));

                get("/graficos", ctx -> {
                    Map<String, Object> json = new HashMap();

                    json.put("graficos", FakeServices.getInstancia().graficas());

                    ctx.status(200);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("graficos", "ver")));

                get("/perros_en_donacion", ctx -> {

                    ctx.json(PerroServices.getInstancia().getPerrosParaDonacion());
                    ctx.status(200);
                }, Collections.singleton(new FakeServices.RolApp("persona", "ver")));


                get("/planes", ctx -> {
                    List<Plan> planes = PlanServices.getInstancia().findAll();
                    Map<String, Object> json = new HashMap();

                    json.put("planes", planes);

                    ctx.status(200);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("planes", "ver")));


                get("/perros/:id_perro/historial_visitas", ctx -> {
                    int status = 200;
                    Map<String, Object> json = new HashMap();


                    Perro perr = PerroServices.getInstancia().find(Integer.valueOf(ctx.pathParam("id_perro")));

                    if (perr != null) {
                        List<HistorialDeVisitas> historial = HistorialDeVisitasService.getInstancia().getHistorialByPerroId(perr);
                        json.put("historial", historial);
                    } else {
                        status = 409;
                        json.put("msg", "El perro no existe");
                    }

                    ctx.json(json);
                    ctx.status(status);
                }, Collections.singleton(new FakeServices.RolApp("historial_de_visitas", "ver")));


                post("/foto", ctx -> {
                    Gson g = new Gson();
//                    try{
                    String base64 = ctx.formParam("fotoBase64");
//                        Foto foto = g.fromJson(ctx.body(), Foto.class) != null ? g.fromJson(ctx.body(), Foto.class) : null;
                    if (base64 != null) {
                        Foto foto = new Foto();
                        FotoServices.getInstancia().crear(foto);
                        String nombre = "foto_" + foto.getId();
                        FakeServices.getInstancia().guardarFoto(base64, nombre);
                        foto.setNombre(nombre + ".png");
                        FotoServices.getInstancia().editar(foto);

                    } else {
                        System.out.println("Foto nula");
                    }

//                    }catch(Exception e){
//
//                            System.out.println("Entro en el catch");
//                            System.out.println(e);
//
//                    }

//                        System.out.println("foto.getNombre() =>");
//                        System.out.println(foto.getNombre());
                }, Collections.singleton(new FakeServices.RolApp("perro", "crear")));

                post("/registrar_perro", ctx -> {
                    int status = 200;
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    JSONObject res = new JSONObject();

//                    Gson g = new Gson();
//                    Perro perroData = g.fromJson(ctx.body(), Perro.class);
//                    System.out.println("Perro inf =>");
//                    System.out.println(perroData.getNombre());


                    if (per != null) {

                        String id_perro = ctx.formParam("RFID_CODE");
                        String nombre = ctx.formParam("nombre");
                        Date fecha_registro = new Date();
                        String base64 = ctx.formParam("fotoBase64");
                        int limite_repeticion_comida = 2;

                        System.out.println("El RFID_CODE =>" + id_perro);
                        System.out.println("El Nombre =>" + nombre);
                        Perro perro = new Perro(id_perro, nombre, fecha_registro, limite_repeticion_comida);


                        if (PerroServices.getInstancia().crear(perro)) {
                            if (base64 != null) {
                                FakeServices.getInstancia().setFotoToEntity(base64, perro);
                            }
                            Subscripcion sub = per.getSubcripciones();
                            int cantidad_de_perros = sub.getPlan().getCantidad_maxima_de_perros();
                            if (per.getUsuario().equalsIgnoreCase("admin") || per.getUsuario().equalsIgnoreCase("pi")) {
                                System.out.println("Entra aqui");
                                sub.addPerro(perro);
                                SubcripcionServices.getInstancia().editar(sub);
                                PersonaServices.getInstancia().editar(per);
                                res.put("msg", "Perro Registrado correctamente!");
                            } else {
                                boolean aux = cantidad_de_perros - sub.getPerros().size() > 0 ? true : false;
                                if (aux) {
                                    sub.addPerro(perro);
                                    SubcripcionServices.getInstancia().editar(sub);
                                    PersonaServices.getInstancia().editar(per);
                                    res.put("msg", "Perro Registrado correctamente!");
                                } else {
                                    res.put("msg", "Usted ha excedido el limite de perros en su plan!");
                                    status = 409;
                                }
                            }


                        } else {
                            res.put("msg", "Fallo la insercion en la base de datos!");
                            status = 409;
                        }
                    } else {
                        res.put("msg", "Usted no tiene acceso a esta accion");
                        status = 401;

                    }


                    ctx.status(status);
                    ctx.json(res.toMap());
                }, Collections.singleton(new FakeServices.RolApp("perro", "crear")));




                post("/subscribirme", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    Plan plan = PlanServices.getInstancia().find(Integer.valueOf(ctx.formParam("id_plan")));
                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));


                    if (persona != null) {
                        if (plan != null) {
                            if (persona.getSubcripciones() == null) {

                                Subscripcion sub = new Subscripcion(plan, new Date(), new Date(), new ArrayList<Perro>());
                                SubcripcionServices.getInstancia().crear(sub);
                                persona.setSubcripciones(sub);
                                PersonaServices.getInstancia().editar(persona);
                                res.put("id_suscripcion", sub.getId_subscripcion());
                                res.put("msg", "Se ha suscrito correctamente!");

                            } else {

                                Subscripcion s = persona.getSubcripciones();

                                if (plan.getId_plan() != s.getPlan().getId_plan()) {
                                    if (FacturacionServices.getInstancia().puedeCambiarDePlan(plan, persona)) {
                                        s.setPlan(plan);
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                        System.out.println("Fecha actual =>" + format.format(new Date()));
                                        System.out.println("Fecha de pago =>" + format.format(s.getFechaVencimientoPago()));
                                        long monthsBetween = ChronoUnit.MONTHS.between(
                                                LocalDate.parse(format.format(new Date())).withDayOfMonth(1),
                                                LocalDate.parse(format.format(s.getFechaVencimientoPago())).withDayOfMonth(1));
                                        System.out.println(monthsBetween); //3

                                        Calendar cal = Calendar.getInstance();
                                        cal.add(Calendar.MONTH, plan.getMeses_actividad() + monthsBetween < 0 ? 0 : (int) monthsBetween);
                                        s.setFechaVencimientoPago(cal.getTime());
                                        SubcripcionServices.getInstancia().editar(s);
                                        res.put("msg", "Ha cambiado de plan!");

                                    } else {
                                        status = 409;
                                        res.put("msg", "No puede cambiar de plan, primero necesita pagar el plan nuevo!");

                                    }


                                } else {
                                    status = 409;
                                    res.put("msg", "Ya esta suscrito a este plan!");
                                }

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

                }, Collections.singleton(new FakeServices.RolApp("suscripcion", "crear")));

                post("/subscribir/perros", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    List<String> id_perros = ctx.formParams("perros");
                    List<Perro> perros = id_perros == null ? null : PerroServices.getInstancia().buscarVariosPerrosPorId(id_perros);
                    Subscripcion sub = persona.getSubcripciones();

                    if (persona != null) {
                        if (perros != null) {
                            String user = persona.getUsuario();

                            if (user.equalsIgnoreCase("admin") || user.equalsIgnoreCase("pi")) {
                                sub.addPerros(perros);
                                SubcripcionServices.getInstancia().editar(sub);
                                res.put("msg", "Se han agregado los perros correctamente!");
                            } else {
                                if (perros.size() == id_perros.size()) {
                                    int restantes = sub.getPlan().getCantidad_maxima_de_perros() - sub.getPerros().size();
                                    boolean aux = (restantes) > 0 ? true : false;
                                    System.out.println("Perros restantes =>" + restantes);
                                    if (aux && restantes <= sub.getPlan().getCantidad_maxima_de_perros()) {
                                        sub.addPerros(perros);
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

                }, Collections.singleton(new FakeServices.RolApp("suscripcion", "editar")));
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
                }, Collections.singleton(new FakeServices.RolApp("vacuna", "editar")));

                post("/pagar", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    Plan plan = ctx.formParam("id_plan") != null ?
                            PlanServices.getInstancia().find(Integer.valueOf(ctx.formParam("id_plan"))) : null;

                    Gson g = new Gson();

                    PaypalInfo paypal = ctx.body() != null ? g.fromJson(ctx.body(), PaypalInfo.class) : null;


                    System.out.println("El usuario = >" + per.getUsuario());
                    System.out.println("El objeto =>");
                    System.out.print(paypal);

                    if (per.getSubcripciones() != null) {
                        Subscripcion sub = per.getSubcripciones();
//                        res.put("msg", FacturacionServices.getInstancia().getUltimaFacturaByPersona(per).getId_factura());
                        if (paypal != null) {
                            PaypalInfoServices.getInstancia().crear(paypal);
                            Factura factura = new Factura(per, new Date(), per.getSubcripciones(), paypal);
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.MONTH, sub.getPlan().getMeses_actividad());
                            sub.setFechaVencimientoPago(cal.getTime());
                            sub.setPago(true);
                            SubcripcionServices.getInstancia().editar(sub);

                            FacturacionServices.getInstancia().crear(factura);

                            res.put("msg", "Su pago ha sido efectuado con Exito");
                        }
                    } else {
                        status = 401;
                        res.put("msg", "Usted no tiene suscripciones");
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());

                }, Collections.singleton(new FakeServices.RolApp("suscripcion", "editar")));

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

                }, Collections.singleton(new FakeServices.RolApp("dispensador", "editar")));


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
                }, Collections.singleton(new FakeServices.RolApp("perro", "ver")));

                post("perro/:id_perro/adoptar", ctx -> {
                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    int perro_id = Integer.valueOf(ctx.pathParam("id_perro"));
                    JSONObject res = new JSONObject();
                    int status = 200;

                    if (persona != null) {
                        Perro perro = PerroServices.getInstancia().find(perro_id);
                        if (perro != null) {

                            if (persona.getSubcripciones().cuposDePerrosRestantes() > 0) {
                                Persona dueno = PerroServices.getInstancia().buscarDueno(perro);
                                dueno.getSubcripciones().borrarPerro(perro);
                                SubcripcionServices.getInstancia().editar(dueno.getSubcripciones());

                                perro.setAdoptado(true);
                                perro.setFecha_adopcion(new Date());
                                PerroServices.getInstancia().editar(perro);
                                persona.getSubcripciones().addPerro(perro);
                                SubcripcionServices.getInstancia().editar(persona.getSubcripciones());
                                res.put("msg", "Felicidades ha adoptado a: " + perro.getNombre());
                            } else {
                                status = 409;
                                res.put("msg", "No tiene espacio suficciente en su plan");

                            }

                        } else {
                            status = 404;
                            res.put("msg", "El perro buscado no ha sido encontrado");

                        }
                    } else {
                        status = 401;
                        res.put("msg", "No tiene permiso para realizar esta accion");
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());
                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));

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
                                FakeServices.getInstancia().enviarCorreoByPersona(dueno, "Alerta de visita de " + perro.getNombre() + "!", "" +
                                        "https://www.google.com/maps?q=" + dispensador.getLatitud() + "," + dispensador.getLongitud() + "&z=17&hl=es\n" + not.getContenido());
//                                FakeServices.getInstancia().sendToTelegram("https://www.google.com/maps?q=" + dispensador.getLatitud() + "," + dispensador.getLongitud() + "&z=17&hl=es\n" + not.getContenido());


//                                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
////                                https://www.google.com/maps?q=19.22111701965332,-70.52653503417969&z=17&hl=es
//                                Message message = Message.creator(
//                                        new com.twilio.type.PhoneNumber("whatsapp:+18296491998"),
//                                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
//                                        "https://www.google.com/maps?q=" + dispensador.getLatitud() + "," + dispensador.getLongitud() + "&z=17&hl=es\n" + not.getContenido())
//                                        .create();

//                                System.out.println(message.getSid());


                                List<UsuariosConectados> conexionesDelDueno = WebSocketControlador.buscarConexionesDeUsuarioConectadoByUser(dueno);
                                System.out.println("Conexiones =>" + conexionesDelDueno.size());

                                Persona per = PersonaServices.getInstancia().find(1);
                                if (conexionesDelDueno.size() > 0) {
                                    Gson gson = new Gson();
                                    JsonParser parser = new JsonParser();
                                    String noti = gson.toJson(not);
                                    JsonObject gsonArr = parser.parse("{type:'notiPerro', data:" + noti +"}").getAsJsonObject();

                                    System.out.println("Enviando  mensaje al dueno");
                                    enviarMensajeAunGrupo(conexionesDelDueno,gsonArr.toString());
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

                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));

                post("/perro/:id/perdido", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    String header = ctx.header("Authorization");
                    int id = Integer.valueOf(ctx.pathParam("id"));

                    try {


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
                    } catch (Exception e) {
                        status = 409;
                        res.put("msg", "Datos invalidos");

                    }
                    ctx.status(status);
                    ctx.json(res.toMap());
                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));

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
                        },
                        Collections.singleton(new FakeServices.RolApp("perro", "editar")));

                get("/notificaciones_no_vistas", ctx -> {
                    Persona perso = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    if (perso != null) {
                        ctx.json(NotificacionesServices.getInstancia().getNotifciacionesNoVistas(perso));
                    } else {
                        ctx.status(401);
                        ctx.json(new JSONObject().put("msg", "No es un usuario valido"));
                    }

                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));
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
                        } else {
                            status = 200;
                            res.put("msg", "Ya se ha visto anteriormente");

                        }
                    } else {
                        res.put("msg", "La notifiacion no existe");
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());

                }, Collections.singleton(new FakeServices.RolApp("notificacion", "editar")));


//                post("/perros/:id_perro/agregar_vacuna", ctx->{
//                    JSONObject res = new JSONObject();
//                    int status = 200;
//                    int id_perro = Integer.valueOf(ctx.pathParam("id_perro"));
//                    JSONObject body = new JSONObject(ctx.body().toString());
//                    Perro perro = PerroServices.getInstancia().find(id_perro);
//
//                    if(perro != null){
//                                System.out.println("Vacunas para domingo =>");
//                                System.out.println(body);
//                            if (body !=null) {
//
//                                JsonObject root;
//
//                                DateFormat df = new SimpleDateFormat("DD/MM/yyyy");
//                                for (Object obj : body.getJSONArray("vacunas")) {
//                                    System.out.println(obj);
//                                    root = new JsonParser().parse(obj.toString()).getAsJsonObject();
//                                    Vacuna aux = VacunaServices.getInstancia().find(root.get("id_vacuna").getAsInt());
//                                    if(aux != null){
//                                        PerroVacuna vacuna = new PerroVacuna(df.parse(root.get("fecha").getAsString()), aux);
//                                        perro.addVacuna(vacuna);
//                                    }
//                                }
//                                PerroServices.getInstancia().editar(perro);
//                                }
//                    }else{
//                        res.put("msg", "El perro no existe");
//                        status = 409;
//                    }
//
//                    ctx.status(status);
//                    ctx.json(res.toMap());
//
//                });
                post("/perros/:id_perro/agregar_vacuna/:id_vacuna", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    int id_perro = Integer.valueOf(ctx.pathParam("id_perro"));
                    int id_vacuna = Integer.valueOf(ctx.pathParam("id_vacuna"));
                    Vacuna vacuna = VacunaServices.getInstancia().find(id_vacuna);
                    Perro perro = PerroServices.getInstancia().find(id_perro);

                    if (perro != null && vacuna != null) {
                        perro.addVacuna(new PerroVacuna(new Date(), vacuna));
                        PerroServices.getInstancia().editar(perro);
                        res.put("msg", "Se agrego vacuna correctamente");
                    } else {
                        res.put("msg", "Parametros proporcionados incorrectos");
                        status = 409;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());

                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));
                post("/perros/:id_perro/quitar_vacuna/:id_vacunacion", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    int id_perro = Integer.valueOf(ctx.pathParam("id_perro"));
                    int id_vacuna = Integer.valueOf(ctx.pathParam("id_vacuna"));
                    Vacuna vacuna = VacunaServices.getInstancia().find(id_vacuna);
                    Perro perro = PerroServices.getInstancia().find(id_perro);

                    if (perro != null && vacuna != null) {
                        PerroVacunaServices.getInstancia().eliminar(id_vacuna);
                        res.put("msg", "Se borros vacuna correctamente");
                    } else {
                        res.put("msg", "Parametros proporcionados incorrectos");
                        status = 409;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());

                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));


                get("/vacunas", ctx -> {
                    ctx.json(VacunaServices.getInstancia().findAll());
                }, Collections.singleton(new FakeServices.RolApp("vacuna", "ver")));

                post("/limpiar_notificaciones", ctx -> {
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    JSONObject res = new JSONObject();
                    int status = 200;
                    if (per != null) {
                        for (Notificaciones not : per.getNotificaciones()) {
                            not.setActiva(false);
                            NotificacionesServices.getInstancia().editar(not);
                        }

                        res.put("msg", "Se han borrado todas las notificaciones");
                    }

                    ctx.json(res);
                    ctx.status(status);

                }, Collections.singleton(new FakeServices.RolApp("notificaciones", "editar")));
                post("/limpiar_notificaciones/:id_notificacion", ctx -> {
                    Persona per = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    JSONObject res = new JSONObject();
                    int status = 200;
                    Notificaciones notificacion = NotificacionesServices.getInstancia().find(Integer.valueOf(ctx.pathParam("id_notificacion")));
                    if (per != null) {
                        if (notificacion != null) {
                            res.put("msg", NotificacionesServices.getInstancia().comprobarSiEsDueno(per, notificacion));
                        }

                    }


                    ctx.json(res);
                    ctx.status(status);

                }, Collections.singleton(new FakeServices.RolApp("notificaciones", "editar")));

                patch("/perro/editar_perro", ctx -> {

                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    System.out.println(ctx.body());
                    GsonBuilder gsonBuilder = new GsonBuilder();

                    gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        @Override
                        public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                                throws JsonParseException {
                            try {
                                return df.parse(json.getAsString());
                            } catch (ParseException e) {
                                return null;
                            }
                        }
                    });
                    Gson g = gsonBuilder.create();
//                    final Gson g =  new GsonBuilder().registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter()).create();
                    Perro perroConCambios = g.fromJson(ctx.body(), Perro.class);

                    System.out.println(perroConCambios.getNombre());

                    int status = 200;
                    Map<String, Object> json = new HashMap();

                    if (persona != null) {
                        if (perroConCambios != null) {
                            if (perroConCambios != null) {
                                Perro perro = PerroServices.getInstancia().find(perroConCambios.getId_perro());
                                if (perro !=null) {
                                    Persona dueno = PerroServices.getInstancia().buscarDueno(perroConCambios);
                                    if (dueno.getId_persona() == persona.getId_persona()) {
                                        if(perro.getFoto() == null ){
                                            if(perroConCambios.getFoto() != null){
                                                if (perroConCambios.getFoto().getId() == null) {
                                                    Foto foto = new Foto();
                                                    FotoServices.getInstancia().crear(foto);
                                                    String nombre = "foto_" + foto.getId();
                                                    FakeServices.getInstancia().guardarFoto(perroConCambios.getFoto().getNombre(), nombre);
                                                    foto.setNombre(nombre + ".png");
                                                    FotoServices.getInstancia().editar(foto);
                                                    perroConCambios.setFoto(foto);
                                                }
                                            }
                                        }else{
                                            Foto foto = perro.getFoto();
                                            System.out.println(foto.getNombre());
                                            FakeServices.getInstancia().guardarFoto(perroConCambios.getFoto().getNombre(),foto.getNombre().split("\\.")[0] );

//                                            FotoServices.getInstancia().editar(foto);
                                            perroConCambios.setFoto(foto);
                                        }

                                            PerroServices.getInstancia().editar(perroConCambios);
                                            json.put("msg", "Cambios guardados correctamente");
                                    } else {
                                        json.put("msg", "No tiene permisos para realizar esta edicion");
                                        status = 405;
                                    }
                                } else {
                                    json.put("msg", "El perro no existe");
                                    status = 403;
                                }
                            }
                        } else {
                            json.put("msg", "No se recibio informacion");
                            status = 400;
                        }
                    } else {
                        json.put("msg", "Su usuario no es valido!");
                        status = 401;

                    }

                    ctx.json(json);
                    ctx.status(status);

                }, Collections.singleton(new FakeServices.RolApp("perro", "editar")));


                /* DELETES

                 * */
                delete("/perro/:id_perro", ctx ->{
                    Perro perro = PerroServices.getInstancia().find(Integer.valueOf(ctx.pathParam("id_perro")));
                    Persona persona = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));

                    int status = 200;
                    Map<String, Object> json = new HashMap();

                    if(persona != null){
                        if(perro != null){
                            Persona dueno = PerroServices.getInstancia().buscarDueno(perro);
                            if(persona.getId_persona() == dueno.getId_persona()){
                                PerroServices.getInstancia().eliminar(perro.getId_perro());

                                json.put("msg", "Perro eliminado correctamente");
                            }else{
                                json.put("msg", "Usted no puede borrar este perro");
                                status = 401;
                            }
                        }else{
                            json.put("msg", "El perro no existe");
                            status = 400;
                        }

                    }else{
                        json.put("msg", "Su usuario no es valido!");
                        status = 403;
                    }
                    ctx.json(json);
                    ctx.status(status);
                },Collections.singleton(new FakeServices.RolApp("perro", "borrar")));



            });








        });
    }




}

