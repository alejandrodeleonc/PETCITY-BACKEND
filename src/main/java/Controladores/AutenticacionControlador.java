package Controladores;

import Encapsulaciones.Foto;
import Encapsulaciones.LoginResponse;
import Encapsulaciones.Persona;
import Services.FakeServices;
import Services.FotoServices;
import Services.PersonaServices;
import Services.RolServices;
import io.javalin.Javalin;
import kong.unirest.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AutenticacionControlador {
    private Javalin app;

    public AutenticacionControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
        app.routes(() -> {
            path("/api/v1/auth", () -> {


                /*
                 *
                 * En esta parte se hace el login
                 * */
                post("/login", ctx -> {
                    JSONObject res = new JSONObject();
                    String user = ctx.formParam("usuario");
                    String pass = ctx.formParam("password");
                    int status = 200;
                    Map<String, Object> json = new HashMap();
                    if (PersonaServices.getInstancia().verifyUser(pass, user) == null) {
                        //TODO: BAD LOGIN VIA REST API
                        status = 401;

                       json.put("msg","Datos proporcionados incorrectos");


                    } else {
                        LoginResponse lr = FakeServices.getInstancia().generacionJsonWebToken(user);
                        Persona persona = PersonaServices.getInstancia().findByUser(user);
                        json.put("persona", persona);
//                        json.put("pagos_atrasados", FakeServices.getInstancia().u);
                        json.put("token", lr.getToken());


                    }
                    ctx.status(status);
                    ctx.json(json);
                });



                /*
                 *
                 * En esta parte se registra un usuario
                 *
                 * */
                post("/registrar", ctx -> {
                    int status = 200;
                    JSONObject res = new JSONObject();
                    Base64.Encoder enc = Base64.getEncoder();
                    String nombre = ctx.formParam("nombre");
                    String correo = ctx.formParam("correo");
                    String direccion = ctx.formParam("direccion");
                    String identificacion = ctx.formParam("identificacion");
                    String fecha = ctx.formParam("fecha_nacimiento");
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    Date fecha_nacimiento = format.parse(fecha);
                    String usuario = ctx.formParam("usuario");
                    String password = ctx.formParam("password");
                    String base64 = ctx.formParam("fotoBase64") ;


                    String coddigo_retiro = enc.encodeToString(usuario.getBytes());

                    if (!PersonaServices.getInstancia().userExists(usuario)) {

                        Persona persona_registrada = new Persona(nombre, identificacion, correo, fecha_nacimiento, direccion, usuario, password, coddigo_retiro);

                        if (PersonaServices.getInstancia().crear(persona_registrada)) {
                            persona_registrada.addRol(RolServices.getInstancia().find(2));
                            if(base64 != null){
                                FakeServices.getInstancia().setFotoToEntity(base64, persona_registrada);
                            }
                            PersonaServices.getInstancia().editar(persona_registrada);
                                res.put("msg", "Usuario Registrado correctamente!");
                        } else {
                            status = 401;
                            res.put("msg", "Fallo la insercion en la base de datos!");
                        }

                    } else {
                        res.put("msg", "Error el usuario ya existe");
                        status = 409;
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());

                });


            });


        });

    }
}
