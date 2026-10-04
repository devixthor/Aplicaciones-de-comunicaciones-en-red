import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Cliente {
    static boolean[][] ocupado = new boolean[10][10];

    static void main() {
        //Tableros del cliente
        boolean[][] disparado = new boolean[10][10];
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        Tablero miTablero = new Tablero(10,10);
        Tablero misTiros = new Tablero(10,10);

        Scanner sc = new Scanner(System.in);
        String tiroGenerado;

        List<Embarcacion> misEmbarcaciones = new ArrayList<>();
        int x1,x2,y1,y2;
        boolean terminarCoordenadas = false;
        boolean subCorrecto = false;
        int contadorEmbarcaciones = 0;
        int contadorImpactos = 0;

        boolean miTurno;
        boolean terminarJuego = false;
        boolean embarcacionHundida, tiroAcertado = false;
        int filaTiroRecibido, colTiroRecibido;
        Embarcacion embarcacionGolpeada = null;
        String tiroRecibido, respuesta;
        boolean tiroCorrecto;

        try{
            Socket cl = new Socket("127.0.0.1", 1234);
            System.out.println("Conexion establecida con el servidor a traves del puerto: " + cl.getLocalPort() + " y la direccion: " + cl.getInetAddress());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());

            String mensajeRecibido = dis.readUTF();
            String mensajeTableroRecibido = dis.readUTF();

            System.out.println(mensajeRecibido);
            System.out.println(mensajeTableroRecibido);

            Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

            boolean subm = false;

            //REGISTRAR SUBMARINO
            //------------------------------------------------------------------------------------------------------------------
            System.out.printf("Ingresa la fila del submarino: ");
            int filaSub = sc.nextInt();

            System.out.printf("ingresa la columna del submarino: ");
            int columnaSub = sc.nextInt();

            while(!subCorrecto){
                if(filaSub < 0 | filaSub > 10 | columnaSub < 0 | columnaSub > 10 ){
                    System.out.println("Error: Coordenada fuera de rango");
                }
                subCorrecto = true;
            }

            Embarcacion sub = new Embarcacion("Submarino");
            sub.agregarCoordenadas(filaSub, columnaSub);
            misEmbarcaciones.add(sub);
            ocupado[filaSub][columnaSub] = true;
            miTablero.registrarSimbolo(filaSub, columnaSub, "S");
            miTablero.actualizarTablero();
            //------------------------------------------------------------------------------------------------------------------

            subm = true;

            //ACTUALIZAR TABLERO
            Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

            //REGISTRAR DESTRUCTORES
            //------------------------------------------------------------------------------------------------------------------
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

                if (!Embarcacion.validaciones(x1, y1, x2, y2, 2, "destructor", ocupado)) {
                    continue;
                }
                // y si sí?
                Embarcacion.ocuparBarco(x1, y1, x2, y2, 2, ocupado);
                Embarcacion des = Embarcacion.colocarEmbarcacion("Destructor", "D", x1, x2, y1, y2, miTablero);
                misEmbarcaciones.add(des);
                contadorEmbarcaciones++;

                if (contadorEmbarcaciones == 3) {
                    terminarCoordenadas = true;
                }
            }
            //------------------------------------------------------------------------------------------------------------------

            //ACTUALIZAR TABLERO
            Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

            contadorEmbarcaciones = 0;
            terminarCoordenadas = false;

            //REGISTRAR CRUCEROS
            //------------------------------------------------------------------------------------------------------------------
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

                if (!Embarcacion.validaciones(x1, y1, x2, y2, 3, "crucero", ocupado)) {
                    continue;
                }

                Embarcacion.ocuparBarco(x1, y1, x2, y2, 3, ocupado);
                Embarcacion cru = Embarcacion.colocarEmbarcacion("Crucero", "C", x1, x2, y1, y2, miTablero);
                misEmbarcaciones.add(cru);
                contadorEmbarcaciones++;

                if (contadorEmbarcaciones == 2) {
                    terminarCoordenadas = true;
                }
            }
            //------------------------------------------------------------------------------------------------------------------

            //ACTUALIZAR TABLERO
            Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

            //REGISTRAR ACORAZADO
            //------------------------------------------------------------------------------------------------------------------
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
            }while (!Embarcacion.validaciones(x1, y1, x2, y2, 4, "acorazado", ocupado));
            //------------------------------------------------------------------------------------------------------------------

            Embarcacion.ocuparBarco(x1, y1, x2, y2, 4, ocupado);
            Embarcacion aco = Embarcacion.colocarEmbarcacion("Acorazado", "A", x1, x2, y1, y2, miTablero);
            misEmbarcaciones.add(aco);
            Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

            System.out.println("Has terminado de colocar tus embarcaciones. Escribe [listo] cuando estes listo para iniciar");
            sc.nextLine();
            String avisoListo = sc.nextLine().trim().toLowerCase();
            dos.writeUTF(avisoListo);
            dos.flush();

            System.out.println("Calculando quien empieza...");
            int turnoAleatorio = dis.readInt();

            if (turnoAleatorio == 0){
                System.out.println("La PC tira primero");
                miTurno = false;
            }else {
                System.out.println("Tu tiras primero");
                miTurno = true;
            }

            while(!terminarJuego){
                if (!miTurno){
                    tiroRecibido = dis.readUTF();
                    jsonNode = objectMapper.readTree(tiroRecibido);
                    filaTiroRecibido = jsonNode.get("fila").asInt();
                    colTiroRecibido = jsonNode.get("columna").asInt();

                    tiroAcertado = false;
                    embarcacionGolpeada = null;

                    for(Embarcacion embarcacion: misEmbarcaciones){
                        if(embarcacion.recibioImpacto(filaTiroRecibido, colTiroRecibido)){
                            tiroAcertado = true;
                            embarcacionGolpeada = embarcacion;
                            break;
                        }
                    }

                    miTablero.registrarSimbolo(filaTiroRecibido, colTiroRecibido, tiroAcertado ? "X" : "O");

                    boolean hundida = tiroAcertado && embarcacionGolpeada.esHundido();
                    if(hundida) System.out.printf("Se ha hundido un " + embarcacionGolpeada.getNombre());

                    boolean todasHundidas = true;
                    for(Embarcacion embarcacion: misEmbarcaciones){
                        if(!embarcacion.esHundido()){
                            todasHundidas = false;
                            break;
                        }
                    }
                    respuesta = JSON.jsonAciertos(embarcacionGolpeada,hundida, todasHundidas);
                    dos.writeUTF(respuesta);
                    dos.flush();
                    Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

                    if(todasHundidas){
                        System.out.printf("Ha ganado la PC");
                        terminarJuego = true;
                    }else{
                        contadorImpactos++;
                        if(!tiroAcertado || contadorImpactos == 3){
                            miTurno = true;
                            contadorImpactos = 0;
                        }
                    }
                }else{
                    tiroCorrecto = false;
                    int tiroFila = 0, tiroColumna = 0;

                    while (!tiroCorrecto) {
                        System.out.print("Ingresa la fila de tiro: ");
                        tiroFila = sc.nextInt();
                        System.out.print("Ingresa la columna de tiro: ");
                        tiroColumna = sc.nextInt();

                        if (tiroFila < 0 || tiroFila > 9 || tiroColumna < 0 || tiroColumna > 9) {
                            System.out.println("Error: Coordenada fuera de rango (0-9)");
                        } else if (disparado[tiroFila][tiroColumna]) {
                            System.out.println("Error: Ya disparaste a esa casilla");
                        } else {
                            tiroCorrecto = true;
                        }
                    }

                    disparado[tiroFila][tiroColumna] = true;
                    tiroGenerado = JSON.generarJSON(tiroFila, tiroColumna);
                    dos.writeUTF(tiroGenerado);
                    dos.flush();

                    String resultado = dis.readUTF();
                    jsonNode = objectMapper.readTree(resultado);

                    String barco = jsonNode.hasNonNull("embarcacion")
                            ? jsonNode.get("embarcacion").asText()
                            : null;

                    boolean golpeado = jsonNode.get("acierto").asBoolean();
                    boolean fin = jsonNode.get("fin").asBoolean();
                    boolean hundida = jsonNode.get("hundida").asBoolean();

                    misTiros.registrarSimbolo(tiroFila, tiroColumna, golpeado ? "X" : "O");

                    if (hundida && jsonNode.hasNonNull("coordenadas")) {
                        for (JsonNode par : jsonNode.get("coordenadas")) {
                            misTiros.registrarSimbolo(par.get(0).asInt(), par.get(1).asInt(), "#");
                        }
                    }

                    if (hundida && jsonNode.hasNonNull("coordenadas")) {
                        for (JsonNode par : jsonNode.get("coordenadas")) {
                            misTiros.registrarSimbolo(par.get(0).asInt(), par.get(1).asInt(), "#");
                        }
                    }

                    Tablero.imprmirTableros(miTablero.actualizarTablero(), misTiros.actualizarTablero());

                    if (golpeado) {
                        System.out.println("Golpeaste a un " + barco + " en la coordenada (" + tiroFila + "," + tiroColumna + ")");
                        if (hundida) {
                            System.out.println("Hundiste un " + barco);
                        }
                        contadorImpactos++;
                        if (!fin && contadorImpactos < 3) {
                            System.out.println("Tira de nuevo");
                        }
                    } else {
                        System.out.println("Tiro fallado");
                        System.out.println("Turno de la PC");
                    }

                    if (fin){
                        System.out.println("Has ganado");
                        terminarJuego = true;
                    }

                    if (!golpeado || contadorImpactos >= 3) {
                        if (golpeado && !fin) {
                            System.out.println("Has usado tus 3 tiros. Turno de la PC");
                        }
                        miTurno = false;
                        contadorImpactos = 0;
                    }
                }
            }

            dis.close();
            dos.close();
            cl.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

