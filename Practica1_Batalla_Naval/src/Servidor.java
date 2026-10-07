import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Servidor {
    static boolean[][] ocupado = new boolean[10][10];

    static void main() {
        String ANSI_VERDE = "\u001B[32m";

        Random random = new Random();
        boolean[][] disparado = new boolean[10][10];
        ObjectMapper objectMapper = new ObjectMapper();
        Tablero tableroServidor = new Tablero(10,10);
        Tablero tableroTiros = new Tablero(10,10);
        JsonNode jsonNode;
        String tiroRecibido;
        List<Embarcacion> embarcacionesAleatorias = new ArrayList<>();
        int i;
        boolean terminarJuego = false;
        boolean turnoServidor;
        boolean esHundida;
        int filaTiroRecibido, colTiroRecibido;
        Embarcacion embarcacionGolpeada = null;
        String respuesta;
        int contadorImpactos = 0;


        try {
            ServerSocket s = new ServerSocket(1234);
            System.out.println(Colores.CYAN + Colores.NEGRITA);
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║                                          ║");
            System.out.println("║        ⚓ SERVIDOR - BATALLA NAVAL ⚓    ║");
            System.out.println("║                                          ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println(Colores.RESET);

            System.out.println(Colores.VERDE + "Servidor iniciado en el puerto " + s.getLocalPort() + Colores.RESET);

            for(;;){
                Socket cl = s.accept();
                System.out.println();
                System.out.println(Colores.VERDE + Colores.NEGRITA);
                System.out.println("╔══════════════════════════════════════════╗");
                System.out.println("║          👤 JUGADOR CONECTADO           ║");
                System.out.println("╚══════════════════════════════════════════╝");
                System.out.println(Colores.RESET);

                System.out.println(Colores.CYAN + "Dirección: " + cl.getInetAddress() + Colores.RESET);
                System.out.println(Colores.AMARILLO + " Generando tablero aleatorio..." + Colores.RESET);
                String mensajeBienvenida = "Bienvenido a batalla naval";
                String mensajeTablero = "Acomoda tus tripulaciones en el tablero de la izquierda ↙️";

                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(cl.getInputStream());

                dos.writeUTF(mensajeBienvenida);
                dos.writeUTF(mensajeTablero);
                dos.flush();

                // LLENADO ALEATORIO DE TABLERO
                //-------------------------------------------------------------
                int filaSubRandom = random.nextInt(10);
                int colSubRandom = random.nextInt(10);
                Embarcacion subRandom = new Embarcacion("Submarino");
                subRandom.agregarCoordenadas(filaSubRandom, colSubRandom);
                embarcacionesAleatorias.add(subRandom);
                ocupado[filaSubRandom][colSubRandom] = true;
                tableroServidor.registrarSimbolo(filaSubRandom, colSubRandom, "S");

                for (i = 0; i < 3; i++) {
                    embarcacionesAleatorias.add(Tablero.llenarEmbarcacionAleatoria("Destructor", 2, tableroServidor, "D", ocupado));
                }

                for(i = 0; i<2; i++){
                    embarcacionesAleatorias.add(Tablero.llenarEmbarcacionAleatoria("Crucero", 3, tableroServidor, "C", ocupado));
                }
                embarcacionesAleatorias.add(Tablero.llenarEmbarcacionAleatoria("Acorazado", 4, tableroServidor, "A", ocupado));
                //-------------------------------------------------------------
                System.out.println();
                System.out.println(Colores.MAGENTA + Colores.NEGRITA);
                System.out.println("╔══════════════════════════════════════════╗");
                System.out.println("║          🤖 FLOTA DE LA PC              ║");
                System.out.println("╚══════════════════════════════════════════╝");
                System.out.println(Colores.RESET);
                Tablero.imprmirTableros(tableroServidor.actualizarTablero(), tableroTiros.actualizarTablero());

                //LOGICA DE JUEGO
                //-------------------------------------------------------------
                System.out.println("Esperando a que el usuario coloque sus embarcaciones...");
                String confirmacion = dis.readUTF();
                System.out.println(Colores.VERDE + "✓ Confirmación recibida: [" + confirmacion + "]" + Colores.RESET);
                if(confirmacion.equals("listo") || confirmacion.equals("Listo") || confirmacion.equals("LISTO")) {
                    System.out.println(Colores.VERDE + Colores.NEGRITA + "✓ ¡El jugador está listo para jugar!" + Colores.RESET);
                    System.out.println(Colores.AMARILLO + "⚔ Sorteando quién comienza..." + Colores.RESET);
                    int turnoAleatorio = random.nextInt(2);

                    System.out.println("Turno generado: " + turnoAleatorio);
                    dos.writeInt(turnoAleatorio);
                    dos.flush();

                    System.out.println("Turno enviado al cliente");
                    if (turnoAleatorio == 0) {
                        System.out.println(Colores.AMARILLO + "━━━━━━━━━━━━━ 🤖 La PC tira primero ━━━━━━━━━━━━━" + Colores.RESET);
                        turnoServidor = true;
                    } else {
                        System.out.println(Colores.MAGENTA + "━━━━━━━━━━━ ⚔ El jugador tira primero ━━━━━━━━━━━" + Colores.RESET);
                        turnoServidor = false;
                    }
                    while (!terminarJuego) {
                        if (!turnoServidor){
                            tiroRecibido = dis.readUTF();
                            jsonNode = objectMapper.readTree(tiroRecibido);
                            filaTiroRecibido = jsonNode.get("fila").asInt();
                            colTiroRecibido = jsonNode.get("columna").asInt();

                            boolean tiroAcertado = false;
                            embarcacionGolpeada = null;

                            for(Embarcacion embarcacion: embarcacionesAleatorias){
                                if(embarcacion.recibioImpacto(filaTiroRecibido, colTiroRecibido)){
                                    tiroAcertado = true;
                                    embarcacionGolpeada = embarcacion;
                                    break;
                                }
                            }

                            if(tiroAcertado){
                                tableroServidor.registrarSimbolo(filaTiroRecibido, colTiroRecibido, "X");
                            }else{
                                tableroServidor.registrarSimbolo(filaTiroRecibido, colTiroRecibido, "O");
                            }

                            boolean hundida = tiroAcertado && embarcacionGolpeada.esHundido();

                            if(hundida){
                                System.out.println("Se ha hundido un " + embarcacionGolpeada.getNombre());
                                for (int[] c : embarcacionGolpeada.getCoordenadas()) {
                                    tableroServidor.registrarSimbolo(c[0], c[1], "#");
                                }
                            }

                            boolean todasHundidas = true;

                            for(Embarcacion embarcacion: embarcacionesAleatorias){
                                if(!embarcacion.esHundido()){
                                    todasHundidas = false;
                                    break;
                                }
                            }

                            respuesta = JSON.jsonAciertos(embarcacionGolpeada,hundida, todasHundidas);
                            dos.writeUTF(respuesta);
                            dos.flush();
                            System.out.println("Turno del usuario");
                            Tablero.imprmirTableros(tableroServidor.actualizarTablero(), tableroTiros.actualizarTablero());

                            if(tiroAcertado){
                                System.out.println(Colores.ROJO + Colores.NEGRITA + "💥 ¡IMPACTO!, El usuario ha golpeado un " + Colores.RESET + embarcacionGolpeada.getNombre());
                                if(hundida){
                                    System.out.println(Colores.ROJO + Colores.NEGRITA + " 🔥☠ ¡SE HA HUNDIDO UN " + embarcacionGolpeada.getNombre() + "!" + Colores.RESET);
                                }
                                if (!todasHundidas && contadorImpactos + 1 < 3) {
                                    System.out.println("El usuario debe tirar de nuevo");
                                }
                            }else{
                                Sonidos.reproducir("Practica1_Batalla_Naval/src/sonido/agua.wav");
                                System.out.println(Colores.AZUL + Colores.NEGRITA + "\n ¡El usuario ha fallado su tiro! 🌊" + Colores.RESET);
                            }

                            if(todasHundidas){
                                System.out.println(Colores.VERDE + Colores.NEGRITA);
                                System.out.println("███████╗██╗███╗   ██╗");
                                System.out.println("██╔════╝██║████╗  ██║");
                                System.out.println("█████╗  ██║██╔██╗ ██║");
                                System.out.println("██╔══╝  ██║██║╚██╗██║");
                                System.out.println("██║     ██║██║ ╚████║");
                                System.out.println("╚═╝     ╚═╝╚═╝  ╚═══╝");
                                System.out.println();
                                System.out.println("     ██╗██╗   ██╗███████╗ ██████╗  ██████╗ ");
                                System.out.println("     ██║██║   ██║██╔════╝██╔════╝ ██╔═══██╗");
                                System.out.println("     ██║██║   ██║█████╗  ██║  ███╗██║   ██║");
                                System.out.println("██   ██║██║   ██║██╔══╝  ██║   ██║██║   ██║");
                                System.out.println("╚█████╔╝╚██████╔╝███████╗╚██████╔╝╚██████╔╝");
                                System.out.println(" ╚════╝  ╚═════╝ ╚══════╝ ╚═════╝  ╚═════╝ ");
                                System.out.println(Colores.RESET);
                                System.out.println("Ha ganado el usuario");
                                terminarJuego = true;
                            }else{
                                contadorImpactos++;
                                if(!tiroAcertado || contadorImpactos == 3){
                                    turnoServidor = true;
                                    contadorImpactos = 0;
                                }
                            }
                        }else{
                            int filaTiroRandom, colTiroRandom;
                            do {
                                filaTiroRandom = random.nextInt(10);
                                colTiroRandom = random.nextInt(10);
                            } while (disparado[filaTiroRandom][colTiroRandom]);

                            disparado[filaTiroRandom][colTiroRandom] = true;
                            String tiroRandom = JSON.generarJSON(filaTiroRandom, colTiroRandom);
                            dos.writeUTF(tiroRandom);
                            dos.flush();

                            String resultado = dis.readUTF();
                            jsonNode = objectMapper.readTree(resultado);

                            String barco = jsonNode.hasNonNull("embarcacion")
                                    ? jsonNode.get("embarcacion").asText()
                                    : null;

                            boolean golpeado = jsonNode.get("acierto").asBoolean();
                            boolean fin = jsonNode.get("fin").asBoolean();
                            boolean hundida = jsonNode.get("hundida").asBoolean();

                            tableroTiros.registrarSimbolo(filaTiroRandom, colTiroRandom, golpeado ? "X" : "O");

                            if(hundida && jsonNode.hasNonNull("coordenadas")){
                                    for (JsonNode par : jsonNode.get("coordenadas")) {
                                        int fila = par.get(0).asInt();
                                        int col  = par.get(1).asInt();
                                        tableroTiros.registrarSimbolo(fila, col, "#");
                                    }
                            }
                            Tablero.imprmirTableros(tableroServidor.actualizarTablero(), tableroTiros.actualizarTablero());
                            if(golpeado){
                                System.out.println(Colores.ROJO + Colores.NEGRITA + "💥 ¡IMPACTO!, La PC ha golpeado un " + barco+ Colores.RESET + " en la coordenada (" + filaTiroRandom + "," + colTiroRandom + ")");
                                if (hundida){
                                    System.out.println(Colores.ROJO + Colores.NEGRITA + " 🔥☠ ¡LA PC HA HUNDIDO UN " + barco + "!" + Colores.RESET);
                                }
                                contadorImpactos++;
                                if (!fin && contadorImpactos < 3){
                                    System.out.println("La PC tira de nuevo");
                                }
                            }else{
                                Sonidos.reproducir("Practica1_Batalla_Naval/src/sonido/agua.wav");
                                System.out.println(Colores.AZUL + Colores.NEGRITA + "\n🌊 ¡La PC ha fallado su tiro! 🌊" + Colores.RESET);
                            }
                            if (fin){
                                System.out.println(Colores.VERDE + Colores.NEGRITA);
                                System.out.println("  ██╗   ██╗██╗ ██████╗████████╗ ██████╗ ██████╗ ██╗ █████╗ ");
                                System.out.println("  ██║   ██║██║██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗██║██╔══██╗");
                                System.out.println("  ██║   ██║██║██║        ██║   ██║   ██║██████╔╝██║███████║");
                                System.out.println("  ╚██╗ ██╔╝██║██║        ██║   ██║   ██║██╔══██╗██║██╔══██║");
                                System.out.println("   ╚████╔╝ ██║╚██████╗   ██║   ╚██████╔╝██║  ██║██║██║  ██║");
                                System.out.println("    ╚═══╝  ╚═╝ ╚═════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═╝");
                                System.out.println(Colores.RESET);
                                terminarJuego = true;
                            }

                            if (!golpeado || contadorImpactos == 3) {
                                turnoServidor = false;
                                contadorImpactos = 0;
                            }
                        }
                    }

                }else{
                    System.out.printf("Confirmacion invalida");
                }
                //-------------------------------------------------------------
                dos.close();
                cl.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
