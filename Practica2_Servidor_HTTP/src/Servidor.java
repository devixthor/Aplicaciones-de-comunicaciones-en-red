import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static final int PUERTO = 8000;
    ExecutorService pool = Executors.newFixedThreadPool(100);
    ServerSocket ss;
    Map<String, MetodoHttp> metodos = new HashMap<>();

    class Manejador extends Thread {
        protected Socket socket;
        protected PrintWriter pw;
        protected BufferedOutputStream bos;
        protected BufferedReader br;
        protected String FileName;
        protected InputStream in;

        public Manejador(Socket _socket) throws Exception {
            this.socket = _socket;
        }

        public void run() {
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bos = new BufferedOutputStream(socket.getOutputStream());
                pw = new PrintWriter(new OutputStreamWriter(bos));
                in = socket.getInputStream();
                String line = leerLinea(in);

                if (line == null) {
                    pw.print("<html><head><title>Servidor WEB");
                    pw.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    pw.print("</body></html>");
                    socket.close();
                    return;
                }

                System.out.println("\n Cliente conectado desde: " + socket.getInetAddress() + " mediante el puerto: " + socket.getPort());
                System.out.println("Datos: " + line);

                String metodo = line.split(" ")[0].toUpperCase();
                MetodoHttp manejador = metodos.get(metodo);
                if (manejador != null) {
                    manejador.atender(line, in, pw, bos);
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
        metodos.put("PUT", new MetodoPut());
        metodos.put("DELETE", new MetodoDelete());
        System.out.println("Iniciando Servidor.......");
        this.ss = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:");
        System.out.println("Esperando por Cliente....");
        for (; ; ) {
            Socket accept = ss.accept();
            pool.execute(new Manejador(accept));
        }
    }

    public static String leerLinea(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\n') break;
            if (b != '\r') baos.write(b);
        }
        if (b == -1 && baos.size() == 0) return null;
        return baos.toString("UTF-8");
    }

    public static void main(String[] args) throws Exception {
        Servidor server = new Servidor();
    }
}



