import Encapsulaciones.*;
import com.oracle.xmlns.webservices.jaxws_databinding.JavaWsdlMappingType;
import io.javalin.Javalin;
import Services.*;
import io.javalin.http.ForbiddenResponse;
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
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64.Decoder;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import static io.javalin.apibuilder.ApiBuilder.*;
import static java.lang.Integer.*;

public class Main {

    public final static String LLAVE_SECRETA = "cRfUjXn2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-";

    public static void main(String[] args) {

        //JAVALIN CONFIG
        Javalin app = Javalin.create(config -> {
//            config.addStaticFiles("/public"); //STATIC FILES -> /resources/public
            config.enableCorsForAllOrigins();
        });
        app.start(8000);
        DBStart.getInstancia().init();
        Persona persona = new Persona("Alejandro", "4021527", new Date(), "j amor",
                "admin", "admin", "sdfsasdf");
        Persona pi = new Persona("Raspberry Pi", "000000", new Date(), "La zursa",
                "pi", "raspberry", "ninguno");
        PersonaServices.getInstancia().editar(persona);
        PersonaServices.getInstancia().editar(pi);

        Format forma = new SimpleDateFormat("dd/MM/yyyy");
        Perro per = new Perro("484849535648495350491310","Billy", forma.format(new Date()), 2) ;
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

//                    System.out.println(headerAutentificacion);
                    if(headerAutentificacion == null || !headerAutentificacion.startsWith(prefijo)){
                        System.out.println("No hay");
                        throw new ForbiddenResponse("No tiene permiso para acceder al recurso");
                    }



                });

                post("/mantenimiento/registrar_perro", ctx ->{
                    int status= 200;
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("RFID_CODE");
                    String nombre = ctx.formParam("nombre");
                    Date fecha_registro = new Date();
                    int limite_repeticion_comida = 2;
                    Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Perro perro = new Perro(id_perro,nombre, formatter.format(fecha_registro), limite_repeticion_comida) ;

                    if(PerroServices.getInstancia().crear(perro)){
                        res.put("msg", "Perro Registrado correctamente!");
                    }else{
                        res.put("msg", "Fallo la insercion en la base de datos!");
                        status=401;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());
                });

                post("/mantenimiento/subscribirme", ctx ->{
                    int status= 200;
                    JSONObject res = new JSONObject();
                    Plan plan = PlanServices.getInstancia().find(Integer.valueOf(ctx.formParam("plan")));
                    Persona person = PersonaServices.getInstancia().find(Integer.valueOf(ctx.formParam("persona")));


                    Subscripcion subscripcion = new Subscripcion(plan, person);
                    if(SubcripcionServices.getInstancia().crear(subscripcion)){
                        res.put("resultado", "Susbcripcion correcta!");
                    }else{
                        res.put("msg", "Fallo la insercion en la base de datos!");
                        status = 401;
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());

                });

                post("/mantenimiento/subscribir/:id/perros", ctx ->{
                    JSONObject res = new JSONObject();
                    int status = 200;
                    int id = ctx.pathParam("id", Integer.class).get();
                    Subscripcion sub = SubcripcionServices.getInstancia().find(id);
                    Map<String, List<String>> id_perros = ctx.formParamMap();
                    System.out.println("Arreglo =>");
                    System.out.println(id_perros.get("perro"));

                    List<SubscripcionPerro> subper = new ArrayList<SubscripcionPerro>();
                    for(String p : id_perros.get("perro")){
                        System.out.println("Perro =>"+p);
                        Perro perro_aux = PerroServices.getInstancia().find(p);
                        if(perro_aux != null){
                        subper.add(new SubscripcionPerro( sub, perro_aux));
                        }
                    }
                    if(subper.size() == id_perros.get("perro").size()){
                        if(SubscripcionPerroServices.getInstancia().crear(subper)){
                        res.put("msg", "Susbcripcion correcta!");
                        }else{
                        res.put("msg", "Error asociando perro!");
                        status = 401;
                        }
                    }else{
                        res.put("msg", "Los parametros proporcionados son incorrectos!");
                        status = 409;
                    }

                    ctx.status(status);
                    ctx.json(res.toMap());

                });
                post("/mantenimiento/vacunas/:id", ctx ->{
                    JSONObject res = new JSONObject();
                    int status = 200;
                    String id = ctx.pathParam("id");

                    Perro perro = PerroServices.getInstancia().find(id);

                    if(perro != null){

                    }else{
                        status = 409;
                    }

                    ctx.status(status);
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



                get("/mantenimiento/perros", ctx -> {
                    Map<String, Object> res = new HashMap<>();

                    List<Perro> perros = PerroServices.getInstancia().findAll();

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(perros);
                    System.out.println(jsonString);
                    res.put("perros", perros);
                    ctx.json(res);
                });

                get("/mantenimiento/usuarios/:id/perros", ctx -> {
                    int id = ctx.pathParam("id", Integer.class).get();
                    Map<String, Object> res = new HashMap<>();
                    Persona person = PersonaServices.getInstancia().find(id);
                    System.out.print("Persona => "+ person.getNombre() +"\n");
                    List<Perro> perros = SubcripcionServices.getInstancia().getPerrosOfAnUser(person);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(perros);
                    System.out.println("El arreglo => "+ jsonString);

                    res.put("perros",perros);
                    ctx.json(res);
                });

                post("/mantenimiento/comio", ctx -> {
                    JSONObject res = new JSONObject();
                    String id_perro = ctx.formParam("perro");
                    Perro perro = PerroServices.getInstancia().find(id_perro);

                    boolean puede_comer = HistorialDeVisitasService.getInstancia().canEat(perro);

                    res.put("puede_comer", puede_comer);
                    ctx.json(res.toMap());
                });



                post("/mantenimiento/perro/:id/perdido", ctx -> {
                    JSONObject res = new JSONObject();
                    int status = 200;
                    String token = ctx.header("Authorization").split("Bearer",2)[1];
                    String id = ctx.pathParam("id");
                    Perro perro = PerroServices.getInstancia().find(id);

//                    System.out.println("ID: " + claims.getId());
//                    System.out.println("Subject: " + claims.getSubject());
//                    System.out.println("Issuer: " + claims.getIssuer());
//                    System.out.println("Usuario: " + );
//                    System.out.println("Expiration: " + claims.getExpiration());
                    if(perro !=null){
                        Persona dueno = SubscripcionPerroServices.getInstancia().getPersonaByPerro(perro.getId_perro());
                        System.out.println("El dueno => "+ dueno.getNombre() );
                        if(dueno != null){
                            Claims claims = Jwts.parser()
                                    .setSigningKey(DatatypeConverter.parseBase64Binary(LLAVE_SECRETA))
                                    .parseClaimsJws(token).getBody();
                           String requestUser =  claims.get("usuario").toString();
                            if(requestUser.equals(dueno.getUsuario())){
                                perro.setPerdido(true);
                                PerroServices.getInstancia().editar(perro);
                                res.put("msg","funciona");
                            }else{
                                res.put("msg","No tiene permiso para modificar este perro");
                            }
                        }else{
                            res.put("msg","Este perro es callejero");
                        }
                    }else{
                        status = 409;
                        res.put("msg","El perro que busca no existe");
                    }



                    ctx.status(status);
                    ctx.json(res.toMap());


                });



                before("/mantenimiento/administracion/*", ctx->{
                    System.out.println("Aqui es donde se manejaran los permisos");
                });


                post("/mantenimiento/administracion/crear_dispensador", ctx ->{
                    int status= 200;
                    JSONObject res = new JSONObject();
                     String id_dispensador = ctx.formParam("dispensador");
                     String longitud = ctx.formParam("longitud");
                     String latitud = ctx.formParam("latitud");
                     String direccion = ctx.formParam("direccion");

                    Dispensador dispensador = new Dispensador(id_dispensador, longitud, latitud,direccion);


                    if(DispensadorServices.getInstancia().crear(dispensador)){
                        res.put("resultado", "Dispensador creado correctamente!");
                    }else{
                        status = 401;
                        res.put("msg", "Fallo la insercion en la base de datos!");
                    }
                    ctx.status(status);
                    ctx.json(res.toMap());
                });

                post("/mantenimiento/administracion/crear_plan", ctx ->{
                     int status = 200;
                     JSONObject res = new JSONObject();
                     String nombre = ctx.formParam("nombre");
                     float costo = Float.parseFloat(ctx.formParam("costo"));
                     int meses_actividad = parseInt(ctx.formParam("meses_actividad"));
                     int cantidad_maxima_de_perros = parseInt(ctx.formParam("cantidad_maxima_de_perros"));


                    Plan plan = new Plan(nombre, costo, meses_actividad, cantidad_maxima_de_perros);

                    if(PlanServices.getInstancia().crear(plan)){
                        res.put("resultado", "Plan creado correctamente!");
                    }else{
                        status = 401;
                        res.put("msg", "Fallo la insercion en la base de datos!");
                    }

                    ctx.status(status);
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
                        int status = 200;
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
                            Persona persona_registrada = new Persona(nombre, identificacion, fecha_nacimiento, direccion,                             usuario, password, coddigo_retiro  );

                            if(PersonaServices.getInstancia().crear(persona_registrada)){
                                res.put("msg", "Usuario Registrado correctamente!");
                            }else{
                                status = 401;
                                res.put("msg", "Fallo la insercion en la base de datos!");
                            }

                        }else{
                            res.put("msg", "Error el usuario ya existe");
                            status = 409;
                        }
                            ctx.status(status);
                            ctx.json(res.toMap());

                    });


                });


            });
        });
    }




private static LoginResponse generacionJsonWebToken(String usuario){
        LoginResponse loginResponse = new LoginResponse();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(LLAVE_SECRETA);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        // creando la trama.
        String jwt = Jwts.builder()
                .setIssuer("PET-CITY")
                .setSubject("VERSION 1")
                .claim("usuario", usuario)
                .signWith(signatureAlgorithm, signingKey)
                .compact();
        loginResponse.setToken(jwt);
    return loginResponse;
}
}