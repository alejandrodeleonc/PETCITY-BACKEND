package Encapsulaciones;

import Services.AccionServices;
import Services.FakeServices;
import Services.HistorialDeVisitasService;
import Services.PermisoServices;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.core.security.Role;

import java.util.Map;


public class PermisosyAcciones {

    JsonObject permiso = new JsonObject();
    private static PermisosyAcciones instancia;
    String jsonAll = "usuario: {'crear':'Crear Usuario', 'editar':'Actualizar Usuario', 'borrar':'Eliminar Usuario', 'ver':'Ver Usuario'},"+
                         "perro:{'crear':'Crear Perro', 'editar':'Actualizar Perro', 'borrar':'Eliminar Perro', 'ver':'Ver Perro'}";
//    String jsonPerro   = "{'crear':'Crear ', 'editar':'Actualizar', 'borrar':'Eliminar', 'ver':'Ver'}";
//    String jsonPerro   = "{'crear':'Crear ', 'editar':'Actualizar', 'borrar':'Eliminar', 'ver':'Ver'}";
//    String jsonPerro   = "{'crear':'Crear ', 'editar':'Actualizar', 'borrar':'Eliminar', 'ver':'Ver'}";
    private PermisosyAcciones() {
        this.permiso = ((JsonObject) JsonParser.parseString("{"+jsonAll+"}"));
    }

    public void createTable(){
        for(Map.Entry<String, JsonElement> entry: this.permiso.entrySet()){
            System.out.println("entry => ");
            System.out.println(entry.getValue().toString());
            System.out.println(entry.getValue());
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

        this.getAccion("usuario", "borrar");
    }

    public String getAccion(String permiso_, String accion){
        System.out.println(".... -> ");
        JsonElement je = this.permiso.getAsJsonObject(permiso_).get(accion);
        String acc = je != null ? je.getAsString() : null;
        System.out.println(acc);
        return acc;
    }

    public static PermisosyAcciones getInstancia(){
        if(instancia==null){
            instancia = new PermisosyAcciones();
        }

        return instancia;
    }


}
