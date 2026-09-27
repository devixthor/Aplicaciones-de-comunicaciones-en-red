import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    static void main() {
        ObjectMapper objectMapper = new ObjectMapper();
        Tablero tablero = new Tablero(10,10);
        boolean terminarJuego = false;
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

                while(!terminarJuego){
                    String jsonRecibido = dis.readUTF();
                    JsonNode jsonNode = objectMapper.readTree(jsonRecibido);
                    int filaRecibida = jsonNode.get("fila").asInt();
                    int columnaRecibida = jsonNode.get("columna").asInt();
                    System.out.println("El usuario ha colocado un submarino en la coordenada (" + filaRecibida + "," + columnaRecibida + ")");
                    tablero.registrarTiro(filaRecibida, columnaRecibida, "*");
                    String tableroEnviado = tablero.actualizarTablero();
                    dos.writeUTF(tableroEnviado);
                    dos.flush();
                }
                dos.close();
                cl.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
