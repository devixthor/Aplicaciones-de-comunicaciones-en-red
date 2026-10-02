import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Cliente {
    static boolean[][] ocupado = new boolean[10][10];

    static boolean validaciones(int x1, int y1, int x2, int y2, int longitud, String nombre) {

        if (x1 < 0 || x1 >= 10 || x2 < 0 || x2 >= 10 || y1 < 0 || y1 >= 10 || y2 < 0 || y2 >= 10) {
            System.out.println("Fuera de rango del tablero.");
            return false;
        }

        if (x1 != x2 && y1 != y2) {
            System.out.println("El barco no puede estar en diagonal.");
            return false;
        }

        if (Math.abs(x2 - x1) + Math.abs(y2 - y1) != longitud - 1) {
            System.out.println("Longitud incorrecta, se deben ocupar " + longitud + " casillas.");
            return false;
        }

        int pasoX = Integer.compare(x2, x1);
        int pasoY = Integer.compare(y2, y1);
        int fila = x1;
        int columna = y1;

        for (int i = 0; i < longitud; i++) {
            if (ocupado[fila][columna]==true) {
                System.out.println("La casilla ya está ocupada por otro barco.");
                return false;
            }
            fila += pasoX;
            columna += pasoY;
        }
        return true;
    }

    static void ocuparBarco(int x1, int y1, int x2, int y2, int longitud) {

        int pasoX = Integer.compare(x2, x1);
        int pasoY = Integer.compare(y2, y1);
        int fila = x1;
        int columna = y1;

        for (int i = 0; i < longitud; i++) {
            ocupado[fila][columna] = true;
            fila += pasoX;
            columna += pasoY;
        }
    }

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
            boolean sub = false;

            //while(!terminarCoordenadas){
            System.out.printf("Ingresa la fila del submarino: ");
            int filaSub = sc.nextInt();

            System.out.printf("ingresa la columna del submarino: ");
            int columnaSub = sc.nextInt();

            if(filaSub < 0 | filaSub > 10 | columnaSub < 0 | columnaSub > 10 ){
                System.out.println("Error: Coordenada fuera de rango");
                //continue;
            }

            if (ocupado[filaSub][columnaSub]) {
                System.out.println("Casilla ocupada.");
            }

            s.agregarCoordenadas(filaSub, columnaSub);
            embarcaciones.add(s);
            ocupado[filaSub][columnaSub] = true;
            String jsonSub = embarcacionEnviada.generarJSON(filaSub, columnaSub);
            dos.writeUTF(jsonSub);
            dos.flush();

            sub = true;

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

                if (!validaciones(x1, y1, x2, y2, 2, "destructor")) {
                    continue;
                }
                // y si sí?
                ocuparBarco(x1, y1, x2, y2, 2);

                coordenadasEmbarcacion = embarcacionEnviada.jsonRango(x1, x2, y1, y2);

                dos.writeUTF(coordenadasEmbarcacion);
                dos.flush();
                contadorEmbarcaciones++;

                if (contadorEmbarcaciones == 3) {
                    terminarCoordenadas = true;
                }
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

                if (!validaciones(x1, y1, x2, y2, 3, "crucero")) {
                    continue;
                }

                ocuparBarco(x1, y1, x2, y2, 3);
                coordenadasEmbarcacion = embarcacionEnviada.jsonRango(x1, x2, y1, y2);
                dos.writeUTF(coordenadasEmbarcacion);
                dos.flush();

                contadorEmbarcaciones++;

                if (contadorEmbarcaciones == 2) {
                    terminarCoordenadas = true;
                }
            }

            tableroRecibido = dis.readUTF();
            System.out.println(tableroRecibido);
            do {
                System.out.println("Ingresa las coordenadas de tu acorazado (4 casillas de longitud)");
                System.out.printf("Fila inicial: ");
                x1 = sc.nextInt();
                System.out.printf("Columna inicial: ");
                y1 = sc.nextInt();
                System.out.printf("Fila final: ");
                x2 = sc.nextInt();
                System.out.printf("Columna final: ");
                y2 = sc.nextInt();
            }while (!validaciones(x1, y1, x2, y2, 4, "acorazado"));

            ocuparBarco(x1, y1, x2, y2, 4);

            coordenadasEmbarcacion = embarcacionEnviada.jsonRango(x1, x2, y1, y2);

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

