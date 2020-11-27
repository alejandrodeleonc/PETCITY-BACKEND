package Services;

import Encapsulaciones.LoginResponse;
import Encapsulaciones.Persona;
import Encapsulaciones.Subscripcion;
import Encapsulaciones.UsuariosConectados;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

public class FakeServices {
    private final static String LLAVE_SECRETA = "cRfUjXn2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-";
    private static FakeServices instancia;

    public static FakeServices getInstancia(){
        if(instancia==null){
            instancia = new FakeServices();
        }
        return instancia;
    }


    public  boolean compararHeaderUser(String header, Persona usuario) {
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


    public  Persona getUserFromHeader(String header){
        Persona persona = null;
        String token = header.split("Bearer", 2)[1];
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(LLAVE_SECRETA))
                .parseClaimsJws(token).getBody();
        String requestUser = claims.get("usuario").toString();
        persona = PersonaServices.getInstancia().findByUser(requestUser);
        if(persona != null){
            return persona;
        }else{
            return null;
        }
    }

    public  LoginResponse generacionJsonWebToken(String usuario) {
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


//    public Map<String, Object> verificarSiPago(Persona persona) {
//        System.out.println("Entra a la funcion importante");
//        System.out.println(persona.getSubcripciones());
//
//        Map<String, Object> json = new HashMap();
//        for(Subscripcion subs :  persona.getSubcripciones()){
//            System.out.println("estado => " + subs.getFechaVencimientoPago().before(new Date()));
//            if(subs.getFechaVencimientoPago().before(new Date()) && !subs.isPago()){
//                json.put(String.valueOf(subs.getId_subscripcion()), false);
//            }
//        }
//        return json;
//    }
}
