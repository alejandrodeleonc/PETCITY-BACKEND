import Encapsulaciones.*;
import io.javalin.Javalin;
import Services.*;
import io.javalin.http.ForbiddenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.lang.reflect.Field;
import java.text.Format;
import java.util.*;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import javax.crypto.SecretKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.google.gson.Gson;

import static io.javalin.apibuilder.ApiBuilder.*;
import static java.lang.Integer.*;

public class Main {

    public final static String LLAVE_SECRETA = "asd12D1234dfr123@#4Fsdcasdd5g78a";

    public static void main(String[] args) {

        //JAVALIN CONFIG
        Javalin app = Javalin.create(config -> {
//            config.addStaticFiles("/public"); //STATIC FILES -> /resources/public
            config.enableCorsForAllOrigins();
        });
        app.start(8000);
        DBStart.getInstancia().init();
        Persona persona = new Persona("Alejandro", 4021527, new Date(), "j amor",
                "admin", "admin", "sdfsasdf");
        Persona pi = new Persona("Raspberry Pi", 000000, new Date(), "La zursa",
                "pi", "raspberry", "ninguno");
        PersonaServices.getInstancia().editar(persona);
        PersonaServices.getInstancia().editar(pi);

        Format forma = new SimpleDateFormat("dd/MM/yyyy");
        Perro per = new Perro("1928DAS","Billy", forma.format(new Date()), 2) ;
        PerroServices.getInstancia().crear(per);

        Plan pl = new Plan("Prueba", Float.parseFloat("870.89") , 3, 3);
        PlanServices.getInstancia().crear(pl);

        Dispensador dispen = new Dispensador("0013A20040A4D103", "1234", "1234","Calle 7");
        DispensadorServices.getInstancia().crear(dispen);
        HistorialDeVisitas vis = new HistorialDeVisitas(per, dispen, new Date() , true);
        HistorialDeVisitasService.getInstancia().crear(vis);

        app.routes(() -> {
            path("/api/v1", ()->{


                before("/mantenimiento/*", ctx->{
                    String header = "Authorization";
                    String prefijo = "Bearer";

                    String headerAutentificacion = ctx.header(header);
                    System.out.println(headerAutentificacion);
                    if(headerAutentificacion == null || !headerAutentificacion.startsWith(prefijo)){
                        System.out.println("No hay");
                        throw new ForbiddenResponse("No tiene permiso para acceder al recurso");
                    }



                });

                post("/mantenimiento/registrar_perro", ctx ->{
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("RFID_CODE");
                    String nombre = ctx.formParam("nombre");
                    Date fecha_registro = new Date();
                    int limite_repeticion_comida = 2;
                    Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Perro perro = new Perro(id_perro,nombre, formatter.format(fecha_registro), limite_repeticion_comida) ;

                    PerroServices.getInstancia().crear(perro);
                    res.put("msg", "Perro Registrado correctamente!");
                    ctx.json(res.toMap());
                });

                post("/mantenimiento/subscribirme", ctx ->{
                    JSONObject res = new JSONObject();
                    Plan plan = PlanServices.getInstancia().find(Integer.valueOf(ctx.formParam("plan")));
                    Persona person = PersonaServices.getInstancia().find(Integer.valueOf(ctx.formParam("persona")));


                    Subscripcion subscripcion = new Subscripcion(plan, person);
                    SubcripcionServices.getInstancia().crear(subscripcion);

                    res.put("resultado", "Susbcripcion correcta!");
                    ctx.json(res.toMap());

                });


                post("/mantenimiento/visita", ctx -> {
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("perro");
                    Perro perro = PerroServices.getInstancia().find(id_perro);
                    if(ctx.header("FROM") != null){
                        int id_dispensador = Integer.valueOf(ctx.header("FROM"));
                        System.out.println("El id =>"+id_dispensador);
                        Dispensador dispensador = DispensadorServices.getInstancia().find(id_dispensador);
                        Date fecha = new Date();
                        boolean is_comio = Boolean.valueOf(ctx.formParam("is_comio"));

                        HistorialDeVisitas visita = new HistorialDeVisitas(perro, dispensador, fecha, is_comio);
                        HistorialDeVisitasService.getInstancia().crear(visita);
                        res.put("msg", "Perro Registrado correctamente!");
                        ctx.json(res.toMap());
                    }else{
                        throw new ForbiddenResponse("Falto el dispensador de origen");
                    }

                });


                get("mantenimiento/perros", ctx -> {
                    Map<String, Object> res = new HashMap<>();

                    List<Perro> perros = PerroServices.getInstancia().findAll();

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(perros);
                    System.out.println(jsonString);
                    res.put("perros", perros);
                    ctx.json(res);
                });

                get("mantenimiento/usuarios/:id/perros", ctx -> {
                    int id = ctx.pathParam("id", Integer.class).get();
                    Map<String, Object> res = new HashMap<>();
                    Persona person = PersonaServices.getInstancia().find(id);
                    List<Perro> perros = SubcripcionServices.getInstancia().getPerrosOfAnUser(person);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(perros);
                    System.out.println("El arreglo => "+ jsonString);

                    res.put("perros",perros);
                    ctx.json(res);
                });

                post("mantenimiento/comio", ctx -> {
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("perro");
                    Perro perro = PerroServices.getInstancia().find(id_perro);

                    boolean puede_comer = HistorialDeVisitasService.getInstancia().canEat(perro);

                    res.put("puede_comer", puede_comer);
                    ctx.json(res.toMap());
                });

                before("/mantenimiento/administracion/*", ctx->{
                    System.out.println("Aqui es donde se manejaran los permisos");
                });


                post("/mantenimiento/administracion/crear_dispensador", ctx ->{
                    JSONObject res = new JSONObject();
                     String id_dispensador = ctx.formParam("dispensador");
                     String longitud = ctx.formParam("longitud");
                     String latitud = ctx.formParam("latitud");
                     String direccion = ctx.formParam("direccion");

                    Dispensador dispensador = new Dispensador(id_dispensador, longitud, latitud,direccion);
                    DispensadorServices.getInstancia().crear(dispensador);
                    res.put("msg", "Dispensador Registrado correctamente!");
                    ctx.json(res.toMap());
                });

                post("/mantenimiento/administracion/crear_plan", ctx ->{
                    JSONObject res = new JSONObject();
                     String nombre = ctx.formParam("nombre");
                     float costo = Float.parseFloat(ctx.formParam("costo"));
                     int meses_actividad = parseInt(ctx.formParam("meses_actividad"));
                     int cantidad_maxima_de_perros = parseInt(ctx.formParam("cantidad_maxima_de_perros"));


                    Plan plan = new Plan(nombre, costo, meses_actividad, cantidad_maxima_de_perros);
                    PlanServices.getInstancia().crear(plan);
                    res.put("msg", "Plan creado correctamente!");
                    ctx.json(res.toMap());
                });






                path("/auth", ()->{


                    /*
                    *
                    * En esta parte se hace el login
                    * */
                    post("/login", ctx ->{
                        String user = ctx.formParam("usuario");
                        String pass = ctx.formParam("password");

                        if(PersonaServices.getInstancia().verifyUser(pass,user) == null){
                            //TODO: BAD LOGIN VIA REST API

                        }else{
                            LoginResponse lr = generacionJsonWebToken(user);

                            ctx.json(lr);
                        }
                    });



                    /*
                    *
                    * En esta parte se registra un usuario
                    *
                    * */
                    post("/registrar", ctx ->{
                        JSONObject res = new JSONObject();
                        Base64.Encoder enc = Base64.getEncoder();
                        String nombre = ctx.formParam("nombre");
                        String direccion = ctx.formParam("direccion");
                        String identificacion = ctx.formParam("identificacion");
                        String fecha = ctx.formParam("fecha_nacimiento");
                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                        Date fecha_nacimiento = format.parse(fecha);
                        String usuario = ctx.formParam("usuario");
                        String password = ctx.formParam("password");
                        String coddigo_retiro = enc.encodeToString(usuario.getBytes());

                        if(!PersonaServices.getInstancia().userExists(usuario)){
                            Persona persona_registrada = new Persona(nombre, parseInt(identificacion), fecha_nacimiento, direccion,                             usuario, password, coddigo_retiro  );

                            PersonaServices.getInstancia().crear(persona_registrada);

                            res.put("msg", "Usuario Registrado correctamente!");
                            ctx.json(res.toMap());
                        }else{
                            res.put("msg", "Error el usuario ya existe");
                            ctx.json(res.toMap());
                        }


                    });


                });


            });
        });
    }




private static LoginResponse generacionJsonWebToken(String usuario){
        LoginResponse loginResponse = new LoginResponse();
        //generando la llave.
        SecretKey secretKey = Keys.hmacShaKeyFor(LLAVE_SECRETA.getBytes());
        //Generando la fecha valida
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(24*60);
        System.out.println("La fecha actual: "+localDateTime.toString());

        // creando la trama.
        String jwt = Jwts.builder()
                .setIssuer("PET-CITY")
                .setSubject("VERSION 1")
                .setExpiration(Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-4))))
                .claim("usuario", usuario)
                .signWith(secretKey)
                .compact();
        loginResponse.setToken(jwt);
    return loginResponse;
}
}