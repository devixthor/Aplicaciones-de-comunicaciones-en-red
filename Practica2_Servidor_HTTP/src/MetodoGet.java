import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

public class MetodoGet implements MetodoHttp {
    @Override
    public void atender(String line, BufferedReader br, PrintWriter pw, BufferedOutputStream bos) throws Exception {
        if (!line.contains("?")) {
            String fileName = getArch(line);
            if (fileName == null || fileName.isEmpty()) {
                SendA("index.html", bos);
            } else {
                SendA(fileName, bos);
            }
            System.out.println(fileName);
        } else {
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
        }
    }

    private static final Map<String, String> MIME_TYPES = Map.ofEntries(
            Map.entry("html", "text/html"),
            Map.entry("htm", "text/html"),
            Map.entry("css", "text/css"),
            Map.entry("js", "application/javascript"),
            Map.entry("json", "application/json"),
            Map.entry("txt", "text/plain"),
            Map.entry("png", "image/png"),
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("gif", "image/gif"),
            Map.entry("svg", "image/svg+xml"),
            Map.entry("ico", "image/x-icon"),
            Map.entry("pdf", "application/pdf"),
            Map.entry("mp3", "audio/mpeg"),
            Map.entry("mp4", "video/mp4")
    );

    private String obtenerMimeType (String nombreArchivo) {
        int punto = nombreArchivo.lastIndexOf('.');
        if(punto != -1 && punto < nombreArchivo.length() - 1){
            String extension = nombreArchivo.substring(punto + 1).toLowerCase();
            if(MIME_TYPES.containsKey(extension)){
                return MIME_TYPES.get(extension);
            }
        }
        String otroMime = java.net.URLConnection.guessContentTypeFromName(nombreArchivo);
        return (otroMime != null) ? otroMime: "application/octet-stream";
    }

    public String getArch(String line) {
        int i;
        int f;
        if (line.toUpperCase().startsWith("GET")) {
            i = line.indexOf("/");
            f = line.indexOf(" ", i);
            return line.substring(i + 1, f);
        }
        return "";
    }

    private File resolverArchivo(String arg) {
        if(arg.contains("..")){
            System.out.println("Accion no permitida");
        }
        File archivo = new File(arg);
        if (!archivo.exists()) {
            File enModulo = new File("Practica2_Servidor_HTTP/Recursos_Servidor", arg);
            if (enModulo.exists()) {
                return enModulo;
            }
        }
        return archivo;
    }

    public void SendA(String arg, BufferedOutputStream bos) {
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
            sb = sb + "Content-Type: " + obtenerMimeType(arg) + "\n";
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
