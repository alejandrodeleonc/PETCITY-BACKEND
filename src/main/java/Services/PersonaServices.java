package Services;


import Encapsulaciones.Persona;

import javax.persistence.EntityManager;
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

    public Persona findByUser(String user){
        Persona persona = null;
        EntityManager em = getEntityManager();
        List<Persona> res = new ArrayList<Persona>();
        try{
            res = em.createNativeQuery("SELECT * FROM PERSONA where USUARIO = '" + user+"'", Persona.class).getResultList();

            if(res != null){
                persona = res.get(0);
            }
        } finally {
            em.close();
        }

        return persona;
    }

    public  boolean userExists(String user){
        boolean aux = false;

        if(findByUser(user) != null){
            aux = true;
        }

        return aux;
    }
}
