import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class MetodoPost implements MetodoHttp {
    @Override
    public void atender(String line, InputStream in, PrintWriter pw, BufferedOutputStream bos) throws Exception {
        int contentLength = 0;
        String headerLine = "", length = "";
        String body = "";
        int totalBytes = 0;
        byte[] buff;
        int leidos;

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
            if (body.contains("&")) {
                StringTokenizer tokens = new StringTokenizer(body, "&");
                while (tokens.hasMoreTokens()) {
                    String token = java.net.URLDecoder.decode(tokens.nextToken(), StandardCharsets.UTF_8);
                    System.out.println("Tokens: " + token);
                }
            }
        }
        pw.println("HTTP/1.0 200 OK");
        pw.flush();
        pw.println("Content-Type: text/html; charset=UTF-8");
        pw.println();
        pw.println("<h1>POST recibido</h1><p>" + body + "</p>");
        pw.flush();
    }
}
