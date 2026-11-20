import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor {
    public static final int PUERTO = 8000;
    ServerSocket ss;

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

                if (line.indexOf("?") == -1) {
                    getArch(line);
                    if (FileName == null || FileName.isEmpty()) {
                        SendA("index.html");
                    } else {
                        SendA(FileName);
                    }
                    System.out.println(FileName);
                } else if (line.toUpperCase().startsWith("GET")) {
                    StringTokenizer tokens = new StringTokenizer(line, "?");
                    String req_a = tokens.nextToken();
                    String req = tokens.nextToken();
                    System.out.println("Token1: " + req_a + "\n");
                    System.out.println("Token2: " + req + "\n");
                    pw.println("HTTP/1.0 200 Okay");
                    pw.flush();
                    pw.println();
                    pw.flush();
                    pw.print("<html><head><title>SERVIDOR WEB");
                    pw.flush();
                    pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
                    pw.flush();
                    pw.print("<h3><b>" + req + "</b></h3>");
                    pw.flush();
                    pw.print("</center></body></html>");
                    pw.flush();
                } else {
                    pw.println("HTTP//1.0 501 Not Implemented");
                    pw.println();
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

        public void getArch(String line) {
            int i;
            int f;
            if (line.toUpperCase().startsWith("GET")) {
                i = line.indexOf("/");
                f = line.indexOf(" ", i);
                FileName = line.substring(i + 1, f);
            }
        }

        private File resolverArchivo(String arg) {
            File archivo = new File(arg);
            if (!archivo.exists()) {
                File enModulo = new File("Practica2_Servidor_HTTP", arg);
                if (enModulo.exists()) {
                    return enModulo;
                }
            }
            return archivo;
        }

        public void SendA(String arg) {
            try {
                int b_leidos = 0;
                File archivo = resolverArchivo(arg);
                BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(archivo));
                byte[] buf = new byte[1024];
                int tam_bloque = 0;
                if (bis2.available() >= 1024) {
                    tam_bloque = 1024;
                } else {
                    bis2.available();
                }

                int tam_archivo = bis2.available();

                String sb = "";
                sb = sb + "HTTP/1.0 200 ok\n";
                sb = sb + "Server: Servidor/1.0 \n";
                sb = sb + "Date: " + new Date() + " \n";
                sb = sb + "Content-Type: text/html \n";
                sb = sb + "Content-Length: " + tam_archivo + " \n";
                sb = sb + "\n";
                bos.write(sb.getBytes());
                bos.flush();

                while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                    bos.write(buf, 0, b_leidos);
                }
                bos.flush();
                bis2.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public Servidor() throws Exception {
        System.out.println("Iniciando Servidor.......");
        this.ss = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:---OK");
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



