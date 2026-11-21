import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class MetodoPost implements MetodoHttp {
    @Override
    public void atender(String line, BufferedReader br, PrintWriter pw, BufferedOutputStream bos) throws Exception {
        int contentLength = 0;
        String headerLine = "", length = "";
        int totalBytes = 0;
        char[] buff;
        int leidos;

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

            String body = new String(buff, 0, totalBytes);
            if (body.contains("&")) {
                StringTokenizer tokens = new StringTokenizer(body, "&");
                while (tokens.hasMoreTokens()) {
                    System.out.println("Tokens: " + tokens.nextToken());
                }
            } else {
                pw.println("HTTP/1.0 200 OK");
                pw.flush();
                pw.println("Content-Type: text/html; charset=UTF-8");
                pw.flush();
                pw.println();
                pw.println(body);
                pw.flush();
            }
        }
    }
}
