package Controladores;

import Encapsulaciones.*;
import Services.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import kong.unirest.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;
import static java.lang.Integer.parseInt;

public class AdministracionControlador {
    private Javalin app;

    public AdministracionControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
        app.routes(() -> {
            path("/api/v1/administracion", () -> {

                post("/crear_vacuna", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
//                    Gson builder = new Gson();
//                    Persona usuarioDeLaPeticion = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    JSONObject body = new JSONObject(ctx.body().toString());

                    if (body != null) {
                        System.out.println("Vacunas para domingo =>");
                        System.out.println(body.getJSONArray("vacunas"));

                        List<Vacuna> lista = new ArrayList<Vacuna>();
                        JsonObject root;

                        DateFormat df = new SimpleDateFormat("DD/MM/yyyy");
                        for (Object obj : body.getJSONArray("vacunas")) {
                            root = new JsonParser().parse(obj.toString()).getAsJsonObject();
                            Vacuna aux = new Vacuna(root.get("nombre").getAsString());
                            lista.add(aux);
                            VacunaServices.getInstancia().crear(aux);

                        }

                        res.put("msg", "Vacuna creada correctamente");

                    } else {
                        status = 409;
                        res.put("msg", "Error no ha propocionado data");
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());


                });
                post("/crear_dispensador", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
//                    String id_dispensador = ctx.formParam("dispensador");
//                    String longitud = ctx.formParam("longitud");
//                    String latitud = ctx.formParam("latitud");
//                    String direccion = ctx.formParam("direccion");
//
//                    Dispensador dispensador = new Dispensador(id_dispensador, longitud, latitud, direccion, null);
                    Gson g = new Gson();

                    Dispensador dispensador = g.fromJson(ctx.body(), Dispensador.class);

                    Dispensador dispensadorAux = DispensadorServices.getInstancia().findBy("DISPENSADOR", dispensador.getDispensador());
                    Sector sector = SectorServices.getInstancia().getSectorAlquePertenece(dispensador);
                    if (dispensadorAux == null) {
                        if (sector != null) {
                            dispensador.setSector(sector);
                            if (DispensadorServices.getInstancia().crear(dispensador)) {
                                res.put("msg", "Dispensador creado correctamente!");
                            } else {
                                status = 401;
                                res.put("msg", "Fallo la insercion en la base de datos!");
                            }
                        } else {
                            status = 403;
                            res.put("msg", "El dispensador necesita pertenecer a un sector");
                        }
                    } else {
                        status = 409;
                        res.put("msg", "Ya existe un dispensador con esta direccion Zigbee");
                    }


                    ctx.status(status);
                    ctx.json(res.toMap());
                }, Collections.singleton(new FakeServices.RolApp("dispensador", "crear")));

                post("/crear_plan", ctx -> {
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

                post("/crear_sectores", ctx -> {

                    int status = 200;
                    JSONObject res = new JSONObject();
                    Gson g = new Gson();
                    System.out.println(ctx.body());
                    Sector sector = g.fromJson(ctx.body(), Sector.class);

                    if (sector != null) {
                        sector.generarColor();
                        SectorServices.auxNotificacion sectorSolapante = SectorServices.getInstancia().calculoSolape(sector);
                        if (sectorSolapante.isError() == false) {
                            SectorServices.getInstancia().crear(sector);
                            res.put("msg", "Se ha registrado el sector correctamente");
                        } else {
                            status = 409;
                            res.put("msg", sectorSolapante.getDetalle());
                        }
                    } else {
                        status = 405;
                        res.put("msg", "Datos proporcionados incorrectos");

                    }
                    ctx.status(status);
                    ctx.json(res.toMap());


                }, Collections.singleton(new FakeServices.RolApp("sectores", "crear")));

                get("/dispensadores", ctx -> {

                    List<Dispensador> dispensadores = DispensadorServices.getInstancia().findAll();


                    Map<String, Object> json = new HashMap();

                    json.put("dispensadores", dispensadores);

                    ctx.status(200);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("dispensador", "ver")));

                get("/sectores", ctx -> {

                    List<Sector> sectores = SectorServices.getInstancia().findAll();


                    Map<String, Object> json = new HashMap();

                    json.put("sectores", sectores);

                    ctx.status(200);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("dispensador", "ver")));

                delete("dispensador/:id_dispensador", ctx -> {
                    int status = 200;
                    Map<String, Object> json = new HashMap();
                    Dispensador dispensador = DispensadorServices.getInstancia().find(Integer.valueOf(ctx.pathParam("id_dispensador")));
                    if (dispensador != null) {
                        DispensadorServices.getInstancia().eliminar(dispensador.getId_dispensador());
                        json.put("msg", "Se ha eliminado el dispensador correctamente");
                    } else {
                        json.put("msg", "No existe el dispensador que intenta borrar");
                        status = 409;
                    }

                    ctx.status(status);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("dispensador", "borrar")));



                delete("sector/:id_sector", ctx -> {
                    int status = 200;
                    Map<String, Object> json = new HashMap();
                    Sector sector = SectorServices.getInstancia().find(Integer.valueOf(ctx.pathParam("id_sector")));
                    if (sector != null) {
                        SectorServices.getInstancia().eliminar(sector.getId_sector());
                        json.put("msg", "Se ha eliminado el sector correctamente");
                    } else {
                        json.put("msg", "No existe el sector que intenta borrar");
                        status = 409;
                    }

                    ctx.status(status);
                    ctx.json(json);
                }, Collections.singleton(new FakeServices.RolApp("sectores", "borrar")));


            });
            });


    }
}
