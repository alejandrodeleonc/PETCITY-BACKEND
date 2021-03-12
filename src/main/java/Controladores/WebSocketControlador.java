package Controladores;

import Encapsulaciones.*;
import Services.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import io.javalin.Javalin;
import io.javalin.http.sse.SseClient;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketControlador {
    private Javalin app;
    public static List<UsuariosConectados> usuariosConectados = new ArrayList<>();
    public static List<SectoresConectados> sectores = new ArrayList<>();
    public static List<SseClient> listaSseUsuario = new ArrayList<>();

    public WebSocketControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas() {
         /*
        __________________________________
                    WEB SOCKET
        __________________________________
        */


        app.get("/enviarMensaje", ctx -> {
            String mensaje = ctx.queryParam("mensaje");
//            enviarMensajeAClientesConectados(mensaje, "rojo");
            ctx.result("Enviando mensaje: " + mensaje);
        }, Collections.singleton(new FakeServices.RolApp("socket", "ver")));

        app.wsBefore("/webSocketServidor", wsHandler -> {
            System.out.println("Filtro para WS antes de la llamada ws");

        });

        app.ws("/webSocketServidor", ws -> {

            ws.onConnect(ctx -> {
                System.out.println("Conexión CON =>  " + ctx.header("Sec-WebSocket-Protocol"));
                System.out.println(ctx.headerMap());


                System.out.println("Conexión Iniciada - " + ctx.getSessionId());

                if (ctx.header("User-Agent") != null) {
                    UsuariosConectados usr = new UsuariosConectados(ctx.header("Sec-WebSocket-Protocol"), ctx.session);
                    usuariosConectados.add(usr);
                } else {
                    enviarMensajeUnicast(ctx.session, "Error falta el header");

                }

            });


            ws.onClose(ctx -> {
                System.out.println("Conexión Cerrada - " + ctx.getSessionId());
                for (UsuariosConectados u : usuariosConectados) {
                    if (u.getSesion() == ctx.session) {
                        usuariosConectados.remove(u);
                        break;
                    }
                }
            });

            ws.onError(ctx -> {
                System.out.println("Ocurrió un error en el WS");
            });

        });
        app.ws("/webSocketServidor/sectores", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Conexión CON =>  " + ctx.header("Sec-WebSocket-Protocol"));
                System.out.println(ctx.headerMap());

                System.out.println("Conexión Iniciada - " + ctx.getSessionId());


            });

            ws.onMessage(ctx -> {
                System.out.println("ctx.message()");
                System.out.println(ctx.message());
                JsonObject sectorJson = ((JsonObject) JsonParser.parseString(ctx.message()));

                if (sectorJson.get("id") != null) {
                    Sector sector = SectorServices.getInstancia().find(Integer.valueOf(sectorJson.get("id").toString()));
                    if (sector != null) {
                        boolean sectorDuplicado = false;
                        for (SectoresConectados se : sectores) {
                            if (se.getSector().getId_sector() == sector.getId_sector()) {
                                sectorDuplicado = true;
                            }
                        }

                        if (!sectorDuplicado) {

                            SectoresConectados sectoConectado = new SectoresConectados(sector, ctx.session);
                            sectores.add(sectoConectado);
                            this.registrarCambiosEnUnSector(sectoConectado, 0);
                            JsonParser parser = new JsonParser();
                            JsonObject gsonArr = parser.parse(String.valueOf("{type:'estado', data:{sector:"+sectoConectado.getSector().getId_sector() +", estado:"+true + "}}")).getAsJsonObject();
                            Persona per = PersonaServices.getInstancia().find(1);
                            enviarMensajeAunGrupo(this.buscarConexionesDeUsuarioConectadoByUser(per),gsonArr.toString() );

                            enviarMensajeUnicast(ctx.session, "Exito -> El sector ya esta en linea");


                        } else {
                            enviarMensajeUnicast(ctx.session, "Error -> El sector que intenta registrar ya esta en linea");

                            ctx.session.close();
                        }

                    } else {
                        enviarMensajeUnicast(ctx.session, "Error -> Sector invalido");
                        ctx.session.close();

                    }
                }else if(sectorJson.get("dispensadoresApagados")!=null){
                    JsonParser parser = new JsonParser();
                    JsonArray gsonArr = parser.parse(String.valueOf(sectorJson.get("dispensadoresApagados").toString())).getAsJsonArray();

                    List<Dispensador> dispensadoresApagados =  new ArrayList<Dispensador>();
                    for(JsonElement element : gsonArr){
                        Dispensador dispensador = DispensadorServices.getInstancia().find(element.getAsJsonObject().get("id_dispensador").getAsInt());
                        if(dispensador !=null ){
                            dispensadoresApagados.add(dispensador);
                        }
                    }
                    List<Sector> sectores = SectorServices.getInstancia().findAll();
                    if(sectores.size() > 0){
                        for(Sector sector : SectorServices.getInstancia().findAll()){
                            String mensaje ="";
                            for(Dispensador dispensador : dispensadoresApagados){
                                if(dispensador.getSector().getId_sector() == sector.getId_sector() ){
//                                    mensaje +="SECTOR_"+sector.getId_sector() +"%0D%0ADISPENSADOR+-%3E"+dispensador.getId_dispensador()+"%0D%0A++++LATITUD%3A+"+ dispensador.getLatitud() + "%0D%0A++++LONGITUD%3A+"+ dispensador.getLongitud()  + "%0D%0AESTA+FUERA+DE+SERVICIO";

                                    dispensador.setEstado(false);

                                }else{
                                    dispensador.setEstado(true);
                                }
                            }
                            FakeServices.getInstancia().sendToTelegram(mensaje);

                        }
                    }


                }


//                UsuariosConectados usr = new UsuariosConectados(ctx.message(), ctx.session);
//                sectores.add(usr);
            });
            ws.onClose(ctx -> {
                for (SectoresConectados s : sectores) {
                    if (s.getSesion() == ctx.session) {
                        System.out.println("El sector SECTOR - " + s.getSector().getNombre() + " Esta fuera de linea");
//                        FakeServices.getInstancia().sendToTelegram("**SECTOR-CAIDO** : "+ s.getSector().getNombre() + "Por lo que **"+s.getSector().getCantidadDispensadores() + " dispensadores** estan fuera de linea" );
//                        FakeServices.getInstancia().sendLocationToTelegram(s.getSector().getLatitud(), s.getSector().getLongitud());
                        Persona per = PersonaServices.getInstancia().find(1);
                        JsonParser parser = new JsonParser();
                        JsonObject gsonArr = parser.parse(String.valueOf("{type:'estado', data:{sector:"+s.getSector().getId_sector() +", estado:"+false + "}}")).getAsJsonObject();

                        enviarMensajeAunGrupo(this.buscarConexionesDeUsuarioConectadoByUser(per),gsonArr.toString() );

                        this.registrarCambiosEnUnSector(s, 1);
                        sectores.remove(s);
                        break;
                    }
                }
            });

        });

        /**
         * Filtro para activarse despues de la llamadas al contexto.
         */
        app.wsAfter("/mensajeServidor", wsHandler -> {
            System.out.println("Filtro para WS despues de la llamada al WS");
            //ejecutar cualquier evento antes...
        });

        //Caso de los Server-sent Events.
        app.sse("/evento-servidor", sseClient -> {
            System.out.println("Agregando cliente para evento del servidor: ");
            sseClient.sendEvent("conectado", "Conectando ");
            listaSseUsuario.add(sseClient);
            sseClient.onClose(() -> {
                listaSseUsuario.remove(sseClient);
            });
        });


        //Enviando el evento...
        new Thread(() -> {
            while (true) {
                List<SseClient> listaTmp = new CopyOnWriteArrayList<>(listaSseUsuario);
                listaTmp.forEach(sseClient -> {
                    System.out.println("Enviando informacion...");
                    sseClient.sendEvent("ping", "" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //    /**
//     * Permite enviar un mensaje al cliente.
//     * Ver uso de la librería: https://j2html.com/
//     *
//     * @param mensaje
//     * @param color
//     */
//    public static void enviarMensajeAClientesConectados(String mensaje, String color) {
//        for (Session sesionConectada : usuariosConectados) {
//            try {
//                sesionConectada.getRemote().sendString(p(mensaje).withClass(color).render());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public static void enviarMensajeUnicast(Session sesionConectada, String mensaje) {
        try {
            sesionConectada.getRemote().sendString(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void enviarMensajeAunGrupo(List<UsuariosConectados> sesionesConectadas, String mensaje) {
        System.out.println("Entra a enviar");
        try {
            for (UsuariosConectados sesionConectada : sesionesConectadas) {
                System.out.println("Sesion de =>" + sesionConectada.getUsuario() + "id =>" + sesionConectada.getSesion().toString());
                sesionConectada.getSesion().getRemote().sendString(mensaje);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<UsuariosConectados> buscarConexionesDeUsuarioConectadoByUser(Persona persona) {

        List<UsuariosConectados> aux = new ArrayList<>();
        for (UsuariosConectados u : WebSocketControlador.usuariosConectados) {
            if (persona.getUsuario().equals(u.getUsuario())) {
                aux.add(u);
            }
        }

        return aux;
    }

    public static void registrarCambiosEnUnSector(SectoresConectados sectoConectado, int estado) {

        Sector sector = SectorServices.getInstancia().find(sectoConectado.getSector().getId_sector());
        if (sector != null) {
            HistorialSectores historial = new HistorialSectores(sectoConectado.getSesion().getRemoteAddress().toString(), new Date());
            if(estado == 0){
                historial.setEstado("CONECTADO");
                sector.setEstado(true);
            }else{
                historial.setEstado("DESCONECTADO");
                sector.setEstado(false);
            }
            sector.setUltimaIp(sectoConectado.getSesion().getRemoteAddress().toString());
            HistorialSectoresServices.getInstancia().crear(historial);
            sector.addRegistroAHistorial(historial);


            SectorServices.getInstancia().editar(sector);
        }
    }


}
