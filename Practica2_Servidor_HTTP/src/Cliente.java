import java.io.*;
import java.net.*;

public class Cliente {
    static void main() {
        System.out.println("Servidor HTTP");
        try {
            //---------------------SOLICITUD AL SERVIDOR--------------------------//
            String urlString = "http://localhost:8000/";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            //---------------------RESPUESTA DEL SERVIDOR--------------------------//

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }

                br.close();
                System.out.println("Respuesta del servidor");
                System.out.printf(response.toString());
            } else {
                System.out.println("La solicitud no fue éxitosa. Código de respuesta" + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
