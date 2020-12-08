package Controladores;

import Encapsulaciones.Dispensador;
import Encapsulaciones.Persona;
import Encapsulaciones.Plan;
import Encapsulaciones.Vacuna;
import Services.DispensadorServices;
import Services.FakeServices;
import Services.PlanServices;
import Services.VacunaServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import kong.unirest.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

                post("/agregar_vacuna", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    Gson builder = new Gson();
                    Persona usuarioDeLaPeticion = FakeServices.getInstancia().getUserFromHeader(ctx.header("Authorization"));
                    JSONObject body = new JSONObject(ctx.body().toString());

                    System.out.println("Vacunas para domingo =>");
                    List<Vacuna> lista = new ArrayList<Vacuna>();
                    JsonObject root;

                    DateFormat df = new SimpleDateFormat("DD/MM/yyyy");
                    for (Object obj : body.getJSONArray("vacunas")) {
                        root = new JsonParser().parse(obj.toString()).getAsJsonObject();
                        Vacuna aux = new Vacuna(root.get("nombre").getAsString(), df.parse(root.get("fecha").getAsString()));
                        lista.add(aux);
                        VacunaServices.getInstancia().crear(aux);

                    }



                });
                post("/crear_dispensador", ctx -> {
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



            });
        });

    }
}
