import java.io.*;

public class MetodoPut implements MetodoHttp {
    @Override
    public void atender(String line, BufferedReader br, PrintWriter pw, BufferedOutputStream bos) throws Exception {
        int contentLength = 0;
        String fileName = getArchivo(line);
        String body = "", headerLine = "", length = "";
        int totalBytes = 0;
        char[] buff;
        int leidos;

        if (fileName == null || fileName.isEmpty() || fileName.contains("..")) {
            pw.println("HTTP/1.0 400 Bad Request");
            pw.println();
            pw.flush();
            return;
        }

        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            if (headerLine.toLowerCase().startsWith("content-length:")) {
                length = headerLine.substring(headerLine.indexOf(":") + 1).trim();
                contentLength = Integer.parseInt(length);
            }
        }
        if (contentLength > 0) {
            buff = new char[contentLength];
            while (totalBytes < contentLength) {
                try {
                    leidos = br.read(buff, totalBytes, contentLength - totalBytes);
                    if (leidos == -1) break;
                    totalBytes += leidos;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            body = new String(buff, 0, totalBytes);
            resolverArchivo(fileName, body , pw);

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

    private void resolverArchivo(String arg, String body, PrintWriter pw) {
        File carpeta = new File("Practica2_Servidor_HTTP/Recursos_Servidor");
        if (!carpeta.exists()) {
            carpeta = new File("Recursos_Servidor");
        }
        File archivo = new File(carpeta, arg);
        boolean existe = archivo.exists();
        try (FileWriter fw = new FileWriter(archivo)){
            fw.write(body);
        } catch (Exception e){
            e.printStackTrace();
        }
        pw.println(existe ? "HTTP/1.0 200 OK" : "HTTP/1.0 201 Created");
        pw.println("Content-Type: text/plain; charset=UTF-8");
        pw.println();
        pw.flush();
    }
}
