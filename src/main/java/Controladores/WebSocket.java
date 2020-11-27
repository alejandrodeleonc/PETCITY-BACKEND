package Controladores;

import Encapsulaciones.Persona;
import Encapsulaciones.UsuariosConectados;
import io.javalin.Javalin;
import io.javalin.http.sse.SseClient;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocket {
    private Javalin app;
    private  List<UsuariosConectados> usuariosConectados = new ArrayList<>();
    private List<SseClient> listaSseUsuario = new ArrayList<>();

    public WebSocket(Javalin app) {
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
        });

        app.wsBefore("/webSocketServidor", wsHandler -> {
            System.out.println("Filtro para WS antes de la llamada ws");

        });

        app.ws("/webSocketServidor", ws -> {

            ws.onConnect(ctx -> {
//                System.out.println("Conexión CON =>  " + ctx.header("Cookie").split("usuario=", 2)[1]);
                System.out.println("Conexión CON =>  " + ctx.header("Sec-WebSocket-Protocol"));
                System.out.println(ctx.headerMap());

                System.out.println("Conexión Iniciada - " + ctx.getSessionId());

                if (ctx.header("User-Agent") != null) {
                    UsuariosConectados usr = new UsuariosConectados(ctx.header("Sec-WebSocket-Protocol"), ctx.session);
                    usuariosConectados.add(usr);
                } else {
                    enviarMensajeUnicast(ctx.session, "Error falta el header");

                }
//                usuariosConectados.add(ctx.session);
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
        try {
            for(UsuariosConectados sesionConectada : sesionesConectadas ){
                System.out.println("Sesion de =>" + sesionConectada.getUsuario() + "id =>" + sesionConectada.getSesion().toString());
                sesionConectada.getSesion().getRemote().sendString(mensaje);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<UsuariosConectados> buscarConexionesDeUsuarioConectadoByUser(Persona persona){
        List<UsuariosConectados> aux = new ArrayList<>();
        for(UsuariosConectados u : usuariosConectados){
            if(persona.getUsuario().equals(u.getUsuario())){
                aux.add(u);
            }
        }

        return aux;
    }
}
