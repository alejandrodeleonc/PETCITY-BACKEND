package Services;


import Encapsulaciones.Persona;

import java.util.ArrayList;
import java.util.List;

public class PersonaServices extends DBManage<Persona>{
    private static PersonaServices instancia;

    private PersonaServices() { super(Persona.class); }

    public static PersonaServices getInstancia(){
        if(instancia==null){
            instancia = new PersonaServices();
        }

        return instancia;
    }

    public Persona verifyUser(String password, String user){

        Persona aux_user = findByUser(user);
        if(aux_user == null){
            return null;
        }else if(aux_user.getPassword().equals(password)){
            return aux_user;
        }else{
            return null;
        }

    }

    private Persona findByUser(String user){
        List<Persona> personas = new ArrayList<Persona>();

        Persona aux = null;
        personas = PersonaServices.getInstancia().findAll();

        for (Persona per: personas) {
            if(per.getUsuario().equals(user)){
                aux = per;
                break;
            }
        }

        return aux;
    }

    public  boolean userExists(String user){
        boolean aux = false;

        if(findByUser(user) != null){
            aux = true;
        }

        return aux;
    }
}
