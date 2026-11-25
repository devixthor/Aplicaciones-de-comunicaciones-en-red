import java.io.*;
import java.util.Locale;

public class MetodoDelete implements MetodoHttp {
    @Override
    public void atender(String line, InputStream in, PrintWriter pw, BufferedOutputStream bos) throws Exception {
        String fileName = getArchivo(line);
        String headerLine;
        while ((headerLine = Servidor.leerLinea(in)) != null && !headerLine.isEmpty()) {
            System.out.println(headerLine);
        }
        if (fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            pw.println("HTTP/1.0 400 solicitud incorrecta");
            pw.println();
            pw.flush();
            return;
        }
        resolverArchivo(fileName, pw);
    }

    public String getArchivo(String line) {
        int i, j;
        if (line.toUpperCase().startsWith("DELETE")) {
            i = line.indexOf("/");
            j = line.indexOf(" ", i);
            return line.substring(i + 1, j);
        }
        return "";
    }

    private void resolverArchivo(String arg, PrintWriter pw) {
        File carpeta = new File("Practica2_Servidor_HTTP/Recursos_Servidor");
        if (!carpeta.exists()) {
            carpeta = new File("Recursos_Servidor");
        }
        File archivo = new File(carpeta, arg);
        boolean existe = archivo.exists() && archivo.isFile();
        boolean eliminado = false;
        if (existe) {
            try {
                eliminado = archivo.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!existe) {
            pw.println("HTTP/1.0 404 Not Found");
        } else if (eliminado) {
            pw.println("HTTP/1.0 200 OK");
        } else {
            pw.println("HTTP/1.0 500 Internal Server Error");
        }
        pw.println("Content-Type: text/plain; charset=UTF-8");
        pw.println();
        pw.flush();
    }
}
