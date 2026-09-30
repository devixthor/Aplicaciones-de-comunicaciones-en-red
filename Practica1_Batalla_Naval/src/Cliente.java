import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Cliente {
    static void main() {
        String tableroRecibido;
        Scanner sc = new Scanner(System.in);
        JSON embarcacionEnviada = new JSON();
        String coordenadasEmbarcacion;
        List<Embarcacion> embarcaciones = new ArrayList<>();
        Embarcacion s = new Embarcacion("submarino");
        int x1,x2,y1,y2;
        boolean terminarCoordenadas = false;
        int contadorEmbarcaciones = 0;

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

            //while(!terminarCoordenadas){
            System.out.printf("Ingresa la fila del submarino: ");
            int filaSub = sc.nextInt();

            System.out.printf("ingresa la columna del submarino: ");
            int columnaSub = sc.nextInt();

            if(filaSub < 0 | filaSub > 10 | columnaSub < 0 | columnaSub > 10 ){
                System.out.println("Error: Coordenada fuera de rango");
                    //continue;
            }

            s.agregarCoordenadas(filaSub, columnaSub);
            embarcaciones.add(s);
            String jsonSub = embarcacionEnviada.generarJSON(filaSub, columnaSub);
            dos.writeUTF(jsonSub);
            dos.flush();

            tableroRecibido = dis.readUTF();
            System.out.println(tableroRecibido);

            System.out.println("Ingresa las coordenadas de tus 3 destructores (2 casillas de longitud)");
            while(!terminarCoordenadas){
                System.out.println("Rango de coordenadas del destructor " + (contadorEmbarcaciones + 1));
                System.out.printf("Fila inicial: ");
                x1 = sc.nextInt();
                System.out.printf("Columna inicial: ");
                y1 = sc.nextInt();
                System.out.printf("Fila final: ");
                x2 = sc.nextInt();
                System.out.printf("Columna final: ");
                y2 = sc.nextInt();

                coordenadasEmbarcacion = embarcacionEnviada.jsonRango(x1,x2,y1,y2);
                dos.writeUTF(coordenadasEmbarcacion);
                dos.flush();

                contadorEmbarcaciones++;
                if(contadorEmbarcaciones == 3) terminarCoordenadas = true;
            }

            tableroRecibido = dis.readUTF();
            System.out.println(tableroRecibido);

            contadorEmbarcaciones = 0;
            terminarCoordenadas = false;

            System.out.println("Ingresa las coordenadas de tus 2 cruceros (3 casillas de longitud");
            while (!terminarCoordenadas){
                System.out.println("Rango de coordenadas del crucero " + (contadorEmbarcaciones + 1));
                System.out.printf("Fila inicial: ");
                x1 = sc.nextInt();
                System.out.printf("Columna inicial: ");
                y1 = sc.nextInt();
                System.out.printf("Fila final: ");
                x2 = sc.nextInt();
                System.out.printf("Columna final: ");
                y2 = sc.nextInt();

                coordenadasEmbarcacion = embarcacionEnviada.jsonRango(x1,x2,y1,y2);
                dos.writeUTF(coordenadasEmbarcacion);
                dos.flush();

                contadorEmbarcaciones++;
                if(contadorEmbarcaciones == 2) terminarCoordenadas = true;
            }

            tableroRecibido = dis.readUTF();
            System.out.println(tableroRecibido);

            System.out.println("Ingresa las coordenadas de tu acorazado (4 casillas de longitud)");
            System.out.printf("Fila inicial: ");
            x1 = sc.nextInt();
            System.out.printf("Columna inicial: ");
            y1 = sc.nextInt();
            System.out.printf("Fila final: ");
            x2 = sc.nextInt();
            System.out.printf("Columna final: ");
            y2 = sc.nextInt();
            coordenadasEmbarcacion = embarcacionEnviada.jsonRango(x1,x2,y1,y2);
            dos.writeUTF(coordenadasEmbarcacion);
            dos.flush();
            tableroRecibido = dis.readUTF();
            System.out.println(tableroRecibido);
            dos.close();
            cl.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

