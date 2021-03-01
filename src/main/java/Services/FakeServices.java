package Services;

import Encapsulaciones.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.swing.filechooser.FileSystemView;
import javax.xml.bind.DatatypeConverter;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.net.URLConnection;

import io.javalin.core.security.Role;

public class FakeServices {
    private final static String LLAVE_SECRETA = "cRfUjXn2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-";
    private static FakeServices instancia;

    public static class RolApp implements Role {
        public String act;
        public String per;
        public Accion accion;

        public RolApp(String per, String p) {
            this.per = per;
            this.act = p;
//            this.getAccion();
        }

        public void getAccion() {
            String accion_ = PermisosyAcciones.getInstancia().getAccion(this.per, this.act);
            this.accion = accion_ != null ? AccionServices.getInstancia().findBy("NOMBRE", "'" + accion_ + "'") : null;
        }
    }

    public static FakeServices getInstancia() {
        if (instancia == null) {
            instancia = new FakeServices();
        }
        return instancia;
    }


    public boolean compararHeaderUser(String header, Persona usuario) {
        boolean aux = false;
        String token = header.split("Bearer", 2)[1];
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(LLAVE_SECRETA))
                .parseClaimsJws(token).getBody();
        String requestUser = claims.get("usuario").toString();
//      System.out.println("ID: " + claims.getId());
//      System.out.println("Subject: " + claims.getSubject());
//      System.out.println("Issuer: " + claims.getIssuer());
//      System.out.println("Usuario: " + );
//      System.out.println("Expiration: " + claims.getExpiration());
        if (requestUser.equals(usuario.getUsuario())) {
            aux = true;

        }
        return aux;
    }


    public boolean enviarCorreoByPersona(Persona persona, String asunto, String contenido) {
        String USER_NAME = "petcitydo@gmail.com";  // GMail user name (just the part before "@gmail.com")
        String PASSWORD = "20160338"; // GMail password
        String RECIPIENT = persona.getCorreo();


        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = {RECIPIENT}; // list of recipient email addresses
        String subject = asunto;
        String body = contenido;

        sendFromGMail(from, pass, to, subject, body);

        return true;

    }

    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public Persona getUserFromHeader(String header) {
        Persona persona = null;
        String token = header.split("Bearer", 2)[1];
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(LLAVE_SECRETA))
                .parseClaimsJws(token).getBody();
        String requestUser = claims.get("usuario").toString();
        persona = PersonaServices.getInstancia().findByUser(requestUser);
        if (persona != null) {
            return persona;
        } else {
            return null;
        }
    }

    public static void sendToTelegram(String text) {

        //Add Telegram token
        String apiToken = "1496018742:AAFuNAzbZFO37FVLcGLzEx66WMNv7FVrJQk";

        //Add chatId
        String chatId = "1170228172";

        String urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId + "&text=" + text;

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginResponse generacionJsonWebToken(String usuario) {
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


    public boolean verificarSielPagoEstaAlDia(Persona persona) {
        System.out.println("Entra a la funcion importante");
        System.out.println(persona.getSubcripciones());

        Factura ultimaFactura = FacturacionServices.getInstancia().getUltimaFacturaByPersona(persona);
//        Date hoy = new Date();
        boolean estado = false;
        Subscripcion sub = persona.getSubcripciones();
        if (ultimaFactura != null) {
            if (sub != null) {
                if (ultimaFactura.getFecha().after(sub.getFechaVencimientoPago())) {
                    sub.setPago(false);
                    SubcripcionServices.getInstancia().editar(sub);
                } else {
                    sub.setPago(true);
                    SubcripcionServices.getInstancia().editar(sub);
                    estado = true;

                }
            }
        }

        return estado;
    }


    public Map<String, Object> graficas() {
        Map<String, Object> graficos = new HashMap();

        graficos.put("ganancias", FacturacionServices.getInstancia().getGanancias());
        graficos.put("perros_beneficiados", PerroServices.getInstancia().cantidadDePerrosSinDueno());
        graficos.put("total_visitas", HistorialDeVisitasService.getInstancia().getCantidadDeVisitas());
        graficos.put("ganancias_mensuales", FacturacionServices.getInstancia().getGananciasMensuales());


        return graficos;
    }

    public String guardarFoto(String base64, String nombre) {
        String aux = null;

//        Path destinationFile = Paths.get("/src/main/resourcces/public", nombre+".jpg");
//        byte dearr[] = Base64.decodeBase64(crntImage);

        FileOutputStream fos = null;

        try {
            byte[] decodedImg = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
            aux = this.getRutaProyecto() + "/src/main/resources/public" + "/" + nombre + ".png";
            System.out.println("Ruta ->" + aux);
            if (Files.exists(Paths.get(aux))){
                Files.delete(Paths.get(aux));
            }

            fos = new FileOutputStream(new File(aux));

            fos.write(decodedImg);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            aux = null;
        } catch (IOException e) {
            e.printStackTrace();
            aux = null;
        }
        //        Files.write(destinationFile, decodedImg);
//        Files.write(destinationFile, decodedImg, );

        return aux;
    }

    public String getRutaProyecto() {
        String ruta = "";
        for (Object s : FileSystems.getDefault().getPath("").toAbsolutePath()) {
            ruta += "/" + s;
        }
        return ruta;
    }

    public boolean setFotoToEntity(String base64, Object  obj) {
        boolean status = false;

        try{
            Boolean is_persona = obj instanceof Persona;
            Persona persona = is_persona ? (Persona) obj : null;
            Perro perro = !is_persona ? (Perro) obj : null;

            Foto foto = is_persona ? persona.getFoto() : perro.getFoto();
            if (foto == null) {
                foto = new Foto();
                FotoServices.getInstancia().crear(foto);
            }
            String nombreFoto = "foto_" + foto.getId();
            this.guardarFoto(base64, nombreFoto);
            foto.setNombre(nombreFoto + ".png");
            FotoServices.getInstancia().editar(foto);
            if (is_persona) {
                persona.setFoto(foto);
                PersonaServices.getInstancia().editar(persona);
            } else {
                perro.setFoto(foto);
                PerroServices.getInstancia().editar(perro);
            }
            status = true;
        }catch(Exception e){
            status = false;
        }


        return status;
    }

}
