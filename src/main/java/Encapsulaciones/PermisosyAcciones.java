package Encapsulaciones;

import Services.AccionServices;
import Services.FakeServices;
import Services.HistorialDeVisitasService;
import Services.PermisoServices;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.core.security.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PermisosyAcciones {

    JsonObject permiso = new JsonObject();
    private static PermisosyAcciones instancia;
    String jsonAll = "persona: {'crear':'Crear Persona', 'editar':'Actualizar Persona', 'borrar':'Eliminar Persona', 'ver':'Ver Persona'},"+
                     "perro:{'crear':'Crear Perro', 'editar':'Actualizar Perro', 'borrar':'Eliminar Perro', 'ver':'Ver Perro'},"+
                     "historial_de_visitas:{'crear':'Crear Historial de visitas', 'editar':'Actualizar Historial de visitas', 'borrar':'Eliminar Historial de visitas', 'ver':'Ver Historial de visitas'}," +
                     "factura: {'crear':'Crear Factura', 'editar':'Actualizar Factura', 'borrar':'Eliminar Factura', 'ver':'Ver Factura'}," +
            "dispensador: {'crear':'Crear Dispensador', 'editar':'Actualizar Dispensador', 'borrar':'Eliminar Dispensador', 'ver':'Ver Dispensador'}, " +
            "planes: {'crear':'Crear Planes', 'editar':'Actualizar Planes', 'borrar':'Eliminar Planes', 'ver':'Ver Planes'}, "+
            "suscripcion: {'crear':'Crear Suscripcion', 'editar':'Actualizar Suscripcion', 'borrar':'Eliminar Suscripcion', 'ver':'Ver Suscripcion'}, " +
            "vacuna: {'crear':'Crear Vacuna', 'editar':'Actualizar Vacuna', 'borrar':'Eliminar Vacuna', 'ver':'Ver Vacuna'},"+
            "notificacion: {'crear':'Crear Notificacion', 'editar':'Actualizar Notificacion', 'borrar':'Eliminar Notificacion', 'ver':'Ver Notificacion'}," +
            "graficos: {'crear':'Crear Graficos', 'editar':'Actualizar Graficos', 'borrar':'Eliminar Graficos', 'ver':'Ver Graficos'}," +
            "socket: {'crear':'Crear socket', 'editar':'Actualizar socket', 'borrar':'Eliminar socket', 'ver':'Ver socket'}," +
            "sectores: {'crear':'Crear Sectores', 'editar':'Actualizar Sectores', 'borrar':'Eliminar Sectores', 'ver':'Ver Sectores'}";
    ;
//    String jsonPerro   = "{'crear':'Crear ', 'editar':'Actualizar', 'borrar':'Eliminar', 'ver':'Ver'}";
//    String jsonPerro   = "{'crear':'Crear ', 'editar':'Actualizar', 'borrar':'Eliminar', 'ver':'Ver'}";
//    String jsonPerro   = "{'crear':'Crear ', 'editar':'Actualizar', 'borrar':'Eliminar', 'ver':'Ver'}";
    private PermisosyAcciones() {
        this.permiso = ((JsonObject) JsonParser.parseString("{"+jsonAll+"}"));
    }

    public void createTable(){
        for(Map.Entry<String, JsonElement> entry: this.permiso.entrySet()){
//            System.out.println("entry => ");
//            System.out.println(entry.getValue().toString());
//            System.out.println(entry.getValue());
            JsonObject acciones = ((JsonObject) JsonParser.parseString(entry.getValue().toString()));

            System.out.println("permiso 00 => ");
            Permiso permiso_ = PermisoServices.getInstancia().findBy("NOMBRE", "'"+entry.getKey()+"'");
            if (permiso_ == null){
                permiso_ = new Permiso(entry.getKey(), true);
                PermisoServices.getInstancia().crear(permiso_);
            }

            for(Map.Entry<String, JsonElement> entry2: acciones.entrySet()){
                Accion accion = AccionServices.getInstancia().findBy("NOMBRE", "'"+entry2.getValue().getAsString()+"'");
                if (accion == null){
                    accion = new Accion(entry2.getValue().getAsString(), permiso_);
                    AccionServices.getInstancia().crear(accion);
                }
            }
        }

//        this.getAccion("usuario", "borrar");
    }

        public String getAccion(String permiso_, String accion){
        JsonElement je = this.permiso.getAsJsonObject(permiso_).get(accion);
        String acc = je != null ? je.getAsString() : null;
        System.out.println(acc);
        return acc;
    }


    public List<Accion> getTodasLasAcciones() {
        List<Accion> aux = new ArrayList<Accion>();
        for (Map.Entry<String, JsonElement> entry : this.permiso.entrySet()) {

            JsonObject acciones = ((JsonObject) JsonParser.parseString(entry.getValue().toString()));

            for (Map.Entry<String, JsonElement> entry2 : acciones.entrySet()) {
                Accion accion = AccionServices.getInstancia().findBy("NOMBRE", "'" + entry2.getValue().getAsString() + "'");
                if(accion !=null){
                    aux.add(accion);
                }
            }

        }
        return aux;
    }

    public static PermisosyAcciones getInstancia(){
        if(instancia==null){
            instancia = new PermisosyAcciones();
        }

        return instancia;
    }


}
