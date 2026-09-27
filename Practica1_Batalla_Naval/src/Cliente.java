import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Cliente {
    static void main() {
        boolean terminarJuego = false;

        try{
            Socket cl = new Socket("127.0.0.1", 1234);
            System.out.println("Conexion establecida con el servidor a traves del puerto: " + cl.getLocalPort() + " y la direccion: " + cl.getInetAddress());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());

            String mensajeRecibido = dis.readUTF();
            String mensajeTableroRecibido = dis.readUTF();

            System.out.println(mensajeRecibido);
            System.out.println(mensajeTableroRecibido);

            String tableroInicialRecibido = dis.readUTF();
            System.out.println(tableroInicialRecibido);

            while(!terminarJuego){
                Scanner inputFila = new Scanner(System.in);
                System.out.println("Ingresa la fila del submarino: ");
                int fila = inputFila.nextInt();

                Scanner inputColumna = new Scanner(System.in);
                System.out.println("ingresa la columna del submarino: ");
                int columna = inputColumna.nextInt();

                JSON coordenadasEnviadas = new JSON();
                String jsonGenerado = coordenadasEnviadas.generarJSON(fila, columna);
                dos.writeUTF(jsonGenerado);
                dos.flush();

                String tableroEnviado = dis.readUTF();
                System.out.println(tableroEnviado);
            }

            dos.close();
            cl.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

