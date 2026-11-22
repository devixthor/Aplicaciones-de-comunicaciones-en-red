import java.io.*;
import java.nio.charset.StandardCharsets;

public class MetodoPut implements MetodoHttp {
    @Override
    public void atender(String line, InputStream in, PrintWriter pw, BufferedOutputStream bos) throws Exception {
        int contentLength = 0;
        String fileName = getArchivo(line);
        String body = "", headerLine = "", length = "";
        int totalBytes = 0;
        byte[] buff;
        int leidos;

        if (fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            pw.println("HTTP/1.0 400 Bad Request");
            pw.println();
            pw.flush();
            return;
        }

        while ((headerLine = Servidor.leerLinea(in)) != null && !headerLine.isEmpty()) {
            if (headerLine.toLowerCase().startsWith("content-length:")) {
                length = headerLine.substring(headerLine.indexOf(":") + 1).trim();
                contentLength = Integer.parseInt(length);
            }
        }
        if (contentLength > 0) {
            buff = new byte[contentLength];
            while (totalBytes < contentLength) {
                try {
                    leidos = in.read(buff, totalBytes, contentLength - totalBytes);
                    if (leidos == -1) break;
                    totalBytes += leidos;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            body = new String(buff, 0, totalBytes, StandardCharsets.UTF_8);
            resolverArchivo(fileName, body , pw, buff, totalBytes);

        }
    }

    public String getArchivo(String line) {
        int i, f;
        if (line.toUpperCase().startsWith("PUT")) {
            i = line.indexOf("/");
            f = line.indexOf(" ", i);
            return line.substring(i + 1, f);
        }
        return "";
    }

    private void resolverArchivo(String arg, String body, PrintWriter pw, byte[] buff, int totalBytes) {
        File carpeta = new File("Practica2_Servidor_HTTP/Recursos_Servidor");
        if (!carpeta.exists()) {
            carpeta = new File("Recursos_Servidor");
        }
        File archivo = new File(carpeta, arg);
        boolean existe = archivo.exists();
        try (FileOutputStream fos = new FileOutputStream(archivo)){
            fos.write(buff, 0, totalBytes);
        } catch (Exception e){
            e.printStackTrace();
        }
        pw.println(existe ? "HTTP/1.0 200 OK" : "HTTP/1.0 201 Created");
        pw.println("Content-Type: text/plain; charset=UTF-8");
        pw.println();
        pw.flush();
    }
}
