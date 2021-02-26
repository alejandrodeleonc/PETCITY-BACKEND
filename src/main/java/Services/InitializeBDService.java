package Services;

import Encapsulaciones.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class InitializeBDService {
    private static InitializeBDService instancia;

    private InitializeBDService() {}

    public static InitializeBDService getInstancia(){
        if(instancia==null){
            instancia = new InitializeBDService();
        }

        return instancia;
    }


    public void intialize(){
        PermisosyAcciones.getInstancia().createTable();
        this.createDefaultUsers();
        this.createDefaultPlans();
        this.createDefaultDispensadores();
        this.createDefaultVacunas();
    }

    private void createDefaultUsers(){

        List<Persona> personas = new ArrayList<Persona>();

        personas.add(new Persona("Alejandro", "4021527","alejandrodelonc15@gmail.com", new Date(), "j amor",
                "admin", "admin", "sdfsasdf"));
        personas.add(new Persona("Raspberry Pi", "000000", "", new Date(), "La zursa",

                "pi", "raspberry", "ninguno"));

        for(Persona per : personas){
            Persona persona = PersonaServices.getInstancia().findBy("USUARIO", "'"+per.getUsuario()+"'");
            if (persona == null){
                PersonaServices.getInstancia().crear(per);
            }
        }


    }

    private void createDefaultPlans(){
        List<Plan> planes = new ArrayList<Plan>();

        planes.add(new Plan("Premium", Float.parseFloat("75") , 6, 7));
        planes.add(new Plan("Estándar", Float.parseFloat("50") , 4, 5));
        planes.add(new Plan("Básico", Float.parseFloat("35") , 2, 3));

        for(Plan plan : planes){
            Plan pl = PlanServices.getInstancia().findBy("NOMBRE", "'"+plan.getNombre()+"'");
            if (pl == null){
                PlanServices.getInstancia().crear(plan);
            }
        }
    }

    private void createDefaultDispensadores(){
        List<Dispensador> dispensadores = new ArrayList<Dispensador>();
        dispensadores.add(new Dispensador("0013A20040A4D103", "-70.6841744", "19.4484629","Puerta 2"));
        dispensadores.add(new Dispensador("0013A20040A4D105", "-70.6838794", "19.448919","Calle 7"));

        for(Dispensador dispensador : dispensadores){
            Dispensador dis = DispensadorServices.getInstancia().findBy("DISPENSADOR", "'"+dispensador.getDispensador()+"'");
            if (dis == null){
                DispensadorServices.getInstancia().crear(dispensador);
            }
        }
    }



    private void createDefaultVacunas(){
        List<Vacuna> vacunas = new ArrayList<Vacuna>();
        vacunas.add(new Vacuna("Rabia"));
        vacunas.add(new Vacuna("Moquillo"));
        vacunas.add(new Vacuna("Polivalente"));
        vacunas.add(new Vacuna("Leishmaniosis"));


        for(Vacuna vacuna : vacunas){
            Vacuna vac = VacunaServices.getInstancia().findBy("NOMBREVACUNA", "'"+vacuna.getNombreVacuna()+"'");
            if (vac == null){
                VacunaServices.getInstancia().crear(vacuna);
            }
        }
    }

    private void  createsubscripcionDefault(){
        Subscripcion sub = new Subscripcion(PlanServices.getInstancia().getMaxPlan(), new Date(), new Date(), new ArrayList<Perro>());
        SubcripcionServices.getInstancia().crear(sub);

        
    }


//    private void createDefaultDispensadores(){
//        List<Dispensador> dispensadores = new ArrayList<Dispensador>();
//        dispensadores.add()
//
//
//        for(Dispensador dispensador : dispensadores){
//            Dispensador dis = DispensadorServices.getInstancia().findBy("DISPENSADOR", "'"+dispensador.getDispensador()+"'");
//            if (dis == null){
//                DispensadorServices.getInstancia().crear(dispensador);
//            }
//        }
//    }



//
//        Subscripcion sub = new Subscripcion(pl, new Date(), new Date(), new ArrayList<Perro>());
//        sub.addPerro(pers);
//        SubcripcionServices.getInstancia().crear(sub);
//        persoo.setSubcripciones(sub);
//        PersonaServices.getInstancia().editar(persoo);
//
//        Permiso permiso = new Permiso("persona", true);
//        PermisoServices.getInstancia().crear(permiso);
//
//        Accion accion = new Accion("crear", permiso);
//        AccionServices.getInstancia().crear(accion);
//
//
//        Rol rol = new Rol("rol1", true);
//        RolServices.getInstancia().crear(rol);
//
//        rol.addAccion(accion);
////        RolServices.getInstancia().editar(rol);
//
//
//        persoo.addRol(rol);
//        PersonaServices.getInstancia().editar(persoo);




}
