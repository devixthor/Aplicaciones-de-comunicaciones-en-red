import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

    static void main() {

        ObjectMapper objectMapper = new ObjectMapper();
        Tablero tablero = new Tablero(10,10);
        String coordenadasRecibidas;
        JsonNode jsonNode;
        boolean terminarCoordenadas = false;
        List<Embarcacion> embarcacionesCliente = new ArrayList<>();
        int i, contadorEmbarcaciones = 0;
        int filaInicio, filaFin, colInicio, colFin;

        try {
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Servidor iniciado en el puerto " + s.getLocalPort());

            for(;;){
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde " + cl.getInetAddress());

                String mensajeBienvenida = "Bienvenido a batalla naval";
                String mensajeTablero = "Acomoda tus tripulaciones en el siguiente tablero";

                String mensajeInput = "Ingresa la coordenada";

                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(cl.getInputStream());

                dos.writeUTF(mensajeBienvenida);
                dos.writeUTF(mensajeTablero);

                String tableroInicial = tablero.actualizarTablero();
                dos.writeUTF(tableroInicial);
                dos.flush();

                String jsonRecibido = dis.readUTF();
                jsonNode = objectMapper.readTree(jsonRecibido);
                int filaSub = jsonNode.get("fila").asInt();
                int columnaSub = jsonNode.get("columna").asInt();

                System.out.println("El usuario ha colocado un submarino en la coordenada (" + filaSub + "," + columnaSub + ")");
                System.out.println("Guardando la embarcacion...");
                Embarcacion sub = new Embarcacion("submarino");
                sub.agregarCoordenadas(filaSub, columnaSub);
                embarcacionesCliente.add(sub);
                tablero.registrarTiro(filaSub, columnaSub, "S");
                String tableroEnviado = tablero.actualizarTablero();

                dos.writeUTF(tableroEnviado);
                dos.flush();

                while(!terminarCoordenadas){
                    coordenadasRecibidas = dis.readUTF();
                    jsonNode = objectMapper.readTree(coordenadasRecibidas);

                    filaInicio = jsonNode.get("fila inicio").asInt();
                    filaFin = jsonNode.get("fila fin").asInt();
                    colInicio = jsonNode.get("columna inicio").asInt();
                    colFin = jsonNode.get("columna fin").asInt();

                    System.out.println("El rango del destructor " + (contadorEmbarcaciones + 1) + " es: (" + filaInicio + "," + colInicio + "),(" + filaFin + "," + colFin + ")");

                    Embarcacion des = new Embarcacion("destructor");
                    if (filaInicio == filaFin) {
                        int inicio = Math.min(colInicio, colFin);
                        int fin = Math.max(colInicio, colFin);
                        for (i = inicio; i <= fin; i++) {
                            des.agregarCoordenadas(filaInicio, i);
                            tablero.registrarTiro(filaInicio, i, "D");
                        }
                    }
                    else if (colInicio == colFin) {
                        int inicio = Math.min(filaInicio, filaFin);
                        int fin = Math.max(filaInicio, filaFin);
                        for (i = inicio; i <= fin; i++) {
                            des.agregarCoordenadas(i, colInicio);
                            tablero.registrarTiro(i, colInicio, "D");
                        }
                    }
                    embarcacionesCliente.add(des);
                    contadorEmbarcaciones++;
                    if(contadorEmbarcaciones == 3) terminarCoordenadas = true;
                }

                dos.writeUTF(tablero.actualizarTablero());
                dos.flush();

                contadorEmbarcaciones = 0;
                terminarCoordenadas = false;

                while(!terminarCoordenadas){
                    coordenadasRecibidas = dis.readUTF();
                    jsonNode = objectMapper.readTree(coordenadasRecibidas);

                    filaInicio = jsonNode.get("fila inicio").asInt();
                    filaFin = jsonNode.get("fila fin").asInt();
                    colInicio = jsonNode.get("columna inicio").asInt();
                    colFin = jsonNode.get("columna fin").asInt();

                    System.out.println("El rango del crucero " + (contadorEmbarcaciones + 1) + " es: (" + filaInicio + "," + colInicio + "),(" + filaFin + "," + colFin + ")");

                    Embarcacion cru = new Embarcacion("crucero");
                    if (filaInicio == filaFin) {
                        int inicio = Math.min(colInicio, colFin);
                        int fin = Math.max(colInicio, colFin);
                        for (i = inicio; i <= fin; i++) {
                            cru.agregarCoordenadas(filaInicio, i);
                            tablero.registrarTiro(filaInicio, i, "C");
                        }
                    }
                    else if (colInicio == colFin) {
                        int inicio = Math.min(filaInicio, filaFin);
                        int fin = Math.max(filaInicio, filaFin);
                        for (i = inicio; i <= fin; i++) {
                            cru.agregarCoordenadas(i, colInicio);
                            tablero.registrarTiro(i, colInicio, "C");
                        }
                    }

                    embarcacionesCliente.add(cru);
                    contadorEmbarcaciones++;
                    if(contadorEmbarcaciones == 2) terminarCoordenadas = true;
                }

                dos.writeUTF(tablero.actualizarTablero());
                dos.flush();

                coordenadasRecibidas = dis.readUTF();
                jsonNode = objectMapper.readTree(coordenadasRecibidas);

                filaInicio = jsonNode.get("fila inicio").asInt();
                filaFin = jsonNode.get("fila fin").asInt();
                colInicio = jsonNode.get("columna inicio").asInt();
                colFin = jsonNode.get("columna fin").asInt();

                System.out.println("El rango del acorazado es: (" + filaInicio + "," + colInicio + "),(" + filaFin + "," + colFin + ")");
                Embarcacion aco = new Embarcacion("acorazado");
                if (filaInicio == filaFin) {
                    int inicio = Math.min(colInicio, colFin);
                    int fin = Math.max(colInicio, colFin);
                    for (i = inicio; i <= fin; i++) {
                        aco.agregarCoordenadas(filaInicio, i);
                        tablero.registrarTiro(filaInicio, i, "A");
                    }
                }
                else if (colInicio == colFin) {
                    int inicio = Math.min(filaInicio, filaFin);
                    int fin = Math.max(filaInicio, filaFin);
                    for (i = inicio; i <= fin; i++) {
                        aco.agregarCoordenadas(i, colInicio);
                        tablero.registrarTiro(i, colInicio, "A");
                    }
                }

                dos.writeUTF(tablero.actualizarTablero());
                dos.flush();

                for (Embarcacion coords : embarcacionesCliente){
                    coords.imprimirCoordenadas();
                }

                dos.close();
                cl.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
