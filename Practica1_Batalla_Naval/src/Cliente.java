import java.io.*;
import java.net.Socket;


public class Cliente {
    static void main() {
        try{
            Socket cl = new Socket("127.0.0.1", 1234);
            System.out.println("Conexion establecida con el servidor a traves del puerto: " + cl.getLocalPort() + " y la direccion: " + cl.getInetAddress());
            //BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            //PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            DataInputStream dis = new DataInputStream(cl.getInputStream());

            String mensajeRecibido = dis.readUTF();

            System.out.println(mensajeRecibido);

            cl.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
