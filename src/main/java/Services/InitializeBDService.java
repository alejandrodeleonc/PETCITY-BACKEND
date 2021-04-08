package Services;

import Encapsulaciones.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.Format;

import java.text.SimpleDateFormat;

public class InitializeBDService {
    private static InitializeBDService instancia;

    private InitializeBDService() {
    }

    private List<Persona> personas = new ArrayList<Persona>();

    public static InitializeBDService getInstancia() {
        if (instancia == null) {
            instancia = new InitializeBDService();
        }

        return instancia;
    }


    public void intialize() {
        PermisosyAcciones.getInstancia().createTable();
        this.createDefaultUsers();
        this.createDefaultPlans();
        this.createDefaultDispensadores();
        this.createDefaultVacunas();
        this.createSubscripcionDefault();
        this.createDefaultDogs();
        this.vacunar();
        this.createDefaultRoles();
        this.createDefaultSectores();
//        this.deleteStaticFiles();

//        System.out.println("Solapa =>"+SectorServices.getInstancia().calculoSolape());
    }

    private void createDefaultUsers() {


        this.personas.add(new Persona("Alejandro", "4021527", "alejandrodelonc15@gmail.com", new Date(), "j amor",
                "admin", "admin", "sdfsasdf"));
        this.personas.add(new Persona("Raspberry Pi", "000000", "", new Date(), "La zursa",

                "pi", "raspberry", "ninguno"));

        for (Persona per : this.personas) {
            Persona persona = PersonaServices.getInstancia().findBy("USUARIO", "'" + per.getUsuario() + "'");
            if (persona == null) {
                PersonaServices.getInstancia().crear(per);
            } else {
                this.personas = new ArrayList<Persona>();

                this.personas.add(persona);
            }
        }


    }

    private void createDefaultPlans() {
        List<Plan> planes = new ArrayList<Plan>();

        planes.add(new Plan("Premium", Float.parseFloat("75"), 6, 7));
        planes.add(new Plan("Estándar", Float.parseFloat("50"), 4, 5));
        planes.add(new Plan("Básico", Float.parseFloat("35"), 2, 3));

        for (Plan plan : planes) {
            Plan pl = PlanServices.getInstancia().findBy("NOMBRE", "'" + plan.getNombre() + "'");
            if (pl == null) {
                PlanServices.getInstancia().crear(plan);
            }
        }
    }
    private void createDefaultSectores() {
        List<Sector> sectores = SectorServices.getInstancia().findAll();

        if(sectores.size() ==0){
            sectores = new ArrayList<Sector>();

//            sectores.add(new Sector("SECTOR 1","6.278235", "-75.5694735", 50003 ));



            sectores.add(new Sector("SECTOR 1","19.441788045504467","-70.68310568230103", 75 ));
            sectores.add(new Sector("SECTOR 2","19.44277905959261", "-70.68417868862701", 75 ));
            sectores.add(new Sector("SECTOR 3","19.445352992807486","-70.6820583739056", 75 ));

            for(Sector sector : sectores){
                SectorServices.getInstancia().crear(sector);
            }

            List<Dispensador>dispensadores = DispensadorServices.getInstancia().findAll();

            if(dispensadores.size() > 0){
                for(Dispensador dispensador : dispensadores){
                    if(dispensador.getSector() ==null){
                        Sector sector = SectorServices.getInstancia().getSectorAlquePertenece(dispensador);
                        if(sector != null){
                            dispensador.setSector(sector);
                            DispensadorServices.getInstancia().editar(dispensador);
                            sector.addDispensador();
                            SectorServices.getInstancia().editar(sector);
                        }
                    }
                }
            }
        }


    }

    private void createDefaultDispensadores() {
        List<Dispensador> dispensadores = new ArrayList<Dispensador>();



        dispensadores.add(new Dispensador("0013A20040A4D103", "-70.68310568230103","19.441788045504467", "Puerta 2", null));
        dispensadores.add(new Dispensador("0013A20040A4D105", "-70.68462927559072","19.4423363221144", "Calle 7", null));
        dispensadores.add(new Dispensador("0013A20040A4D108", "-70.68206452054869","19.44557114575386", "Calle 7", null));

        for (Dispensador dispensador : dispensadores) {

            Dispensador dis = DispensadorServices.getInstancia().findBy("DISPENSADOR", "'" + dispensador.getDispensador() + "'");
            if (dis == null) {
                DispensadorServices.getInstancia().crear(dispensador);
            }
        }

    }


    private void createDefaultVacunas() {
        List<Vacuna> vacunas = new ArrayList<Vacuna>();
        vacunas.add(new Vacuna("Rabia"));
        vacunas.add(new Vacuna("Moquillo"));
        vacunas.add(new Vacuna("Polivalente"));
        vacunas.add(new Vacuna("Leishmaniosis"));


        for (Vacuna vacuna : vacunas) {
            Vacuna vac = VacunaServices.getInstancia().findBy("NOMBREVACUNA", "'" + vacuna.getNombreVacuna() + "'");
            if (vac == null) {
                VacunaServices.getInstancia().crear(vacuna);
            }
        }
    }

    private void createSubscripcionDefault() {

        for (Persona persona : this.personas) {
            if (persona.getSubcripciones() == null) {
                Subscripcion sub = new Subscripcion(PlanServices.getInstancia().getMaxPlan(), new Date(), new Date(), new ArrayList<Perro>());
                SubcripcionServices.getInstancia().crear(sub);
                persona.setSubcripciones(sub);
                PersonaServices.getInstancia().editar(persona);
            }
        }

    }

    //    Format forma = new SimpleDateFormat("dd/MM/yyyy");
//
//        PerroServices.getInstancia().crear(pers);
    private void createDefaultDogs() {

        Persona persona = this.personas.get(0);
        if (persona.getSubcripciones() != null) {

            if (persona.getSubcripciones().getPerros().size() == 0) {


                List<Perro> perros = new ArrayList<Perro>();
                perros.add(new Perro("484849535648495350491310", "Billy", new Date(), 2));
                for (int i = 0; i < 40; i++) {
                    perros.add(new Perro("QWERTYUIOPASDFGG" + i, "PERRO" + (i + 1), new Date(), 2));

                }

                boolean canCreate = true;
                for (Perro perro : perros) {
                    if (PerroServices.getInstancia().findBy("RFID_CODE", "'" + perro.getRfid_code() + "'") != null) {
                        canCreate = false;
                        break;
                    }
                }
                if (canCreate) {
                    PerroServices.getInstancia().crear(perros);
                    persona.getSubcripciones().setPerros(perros);
                    SubcripcionServices.getInstancia().editar(persona.getSubcripciones());
                }
            }
        }


    }

    private void vacunar() {
        List<Perro> perros = PerroServices.getInstancia().findAll();

        if (perros != null) {
            int i = 0;
            for (Perro perro : perros) {
                if (perro.getVacunas().size() == 0) {
                    perro.addVacuna(new PerroVacuna(new Date(), VacunaServices.getInstancia().find(i)));
                    PerroServices.getInstancia().editar(perro);

                    if (i >= 4) {
                        i = 0;
                    }
                    i++;

                }
            }
        }
    }

    private void createDefaultRoles() {


        List<Rol> roles = RolServices.getInstancia().findAll() == null ? null : RolServices.getInstancia().findAll();
        List<Accion> acciones = PermisosyAcciones.getInstancia().getTodasLasAcciones();

        System.out.println("acciones.size() > 0 ->");
        System.out.print(acciones.size() > 0);
        System.out.print("roles == nul ->");
        System.out.print(roles == null);
        if (roles.size() == 0 && acciones.size() > 0) {
            Rol rol = new Rol("ADMINISTRADOR", true);
            RolServices.getInstancia().crear(rol);
            rol.setAcciones(acciones);
            for (Persona persona : this.personas) {
                persona.addRol(rol);
                PersonaServices.getInstancia().editar(persona);
            }

            RolServices.getInstancia().editar(rol);


            acciones = new ArrayList<Accion>();
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("perro", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("perro", "crear") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("perro", "editar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("perro", "borrar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("persona", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("persona", "editar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("factura", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("factura", "crear") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("planes", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("vacuna", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("graficos", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("socket", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("socket", "editar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("socket", "crear") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("suscripcion", "crear") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("suscripcion", "editar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("suscripcion", "borrar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("suscripcion", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("notificacion", "ver") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("notificacion", "crear") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("notificacion", "editar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("notificacion", "borrar") + "'"));
            acciones.add(AccionServices.getInstancia().findBy("NOMBRE", "'" + PermisosyAcciones.getInstancia().getAccion("historial_de_visitas", "ver") + "'"));

            rol = new Rol("USUARIO", true);
            RolServices.getInstancia().crear(rol);
            rol.setAcciones(acciones);
            RolServices.getInstancia().editar(rol);

            System.out.println(rol.getNombre());
            for (Accion accion : rol.getAcciones()) {
                System.out.println(accion.getNombre());
            }
        }


    }

//    public void deleteStaticFiles() {
//        String aux = FakeServices.getInstancia().getRutaProyecto() + "/src/main/resources/public" + "/";
//        System.out.println(aux);
//
//        File folder = new File(aux);
//        File[] listOfFiles = folder.listFiles();
//
//        if (listOfFiles.length > 0) {
//            for (File file : listOfFiles) {
//                if (file.isFile()) {
//                    System.out.println(file.getAbsolutePath());
//                    try {
//                        Files.delete(Paths.get(file.getAbsolutePath()));
//                    } catch (Exception e){
//                        System.out.println("No borro");
//                    }
//                }
//            }
//        }
//    }


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
