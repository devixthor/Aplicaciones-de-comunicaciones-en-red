import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor {
    public static final int PUERTO = 8000;
    ServerSocket ss;
    Map<String, MetodoHttp> metodos = new HashMap<>();

    class Manejador extends Thread {
        protected Socket socket;
        protected PrintWriter pw;
        protected BufferedOutputStream bos;
        protected BufferedReader br;
        protected String FileName;

        public Manejador(Socket _socket) throws Exception {
            this.socket = _socket;
        }

        public void run() {
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bos = new BufferedOutputStream(socket.getOutputStream());
                pw = new PrintWriter(new OutputStreamWriter(bos));
                String line = br.readLine();

                if (line == null) {
                    pw.print("<html><head><title>Servidor WEB");
                    pw.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    pw.print("</body></html>");
                    socket.close();
                    return;
                }

                System.out.println("\n Cliente conectado desde: " + socket.getInetAddress() + "mediante el puerto: " + socket.getPort());
                System.out.println("Datos: " + line);

                String metodo = line.split(" ")[0].toUpperCase();
                MetodoHttp manejador = metodos.get(metodo);
                if (manejador != null) {
                    manejador.atender(line, br, pw, bos);
                } else {
                    pw.println("HTTP/1.0 501 Not Implemented");
                    pw.println();
                    pw.flush();
                }
                pw.flush();
                bos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Servidor() throws Exception {
        metodos.put("GET", new MetodoGet());
        metodos.put("POST", new MetodoPost());
        System.out.println("Iniciando Servidor.......");
        this.ss = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:");
        System.out.println("Esperando por Cliente....");
        for (; ; ) {
            Socket accept = ss.accept();
            new Manejador(accept).start();
        }
    }

    public static void main(String[] args) throws Exception {
        Servidor server = new Servidor();
    }
}



