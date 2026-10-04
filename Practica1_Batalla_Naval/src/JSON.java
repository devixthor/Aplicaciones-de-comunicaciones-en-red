import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSON {
    static ObjectMapper objectMapper = new ObjectMapper();
    int fila;
    int columna;

    public JSON() {
    }

    static String generarJSON(int fila, int columna){
        String resultado = null;
        try{
            ObjectNode objectoJson = objectMapper.createObjectNode();
            objectoJson.put("fila", fila);
            objectoJson.put("columna", columna);
            resultado =  objectMapper.writeValueAsString(objectoJson);
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    static String jsonAciertos(Embarcacion embarcacion, boolean hundida, boolean terminarJuego){
        String resultado = null;
        try{
           ObjectNode objectoJson = objectMapper.createObjectNode();
           boolean acierto =(embarcacion != null);
           objectoJson.put("acierto", acierto);
           objectoJson.put("hundida", hundida);
           objectoJson.put("fin", terminarJuego);
           if (acierto){
               objectoJson.put("embarcacion", embarcacion.getNombre());
           }
           if(hundida){
               ArrayNode coords = objectoJson.putArray("coordenadas");
               for (int[] c : embarcacion.getCoordenadas()) {
                   coords.add(objectMapper.createArrayNode().add(c[0]).add(c[1]));
               }
           }

           resultado = objectMapper.writeValueAsString(objectoJson);
       }catch (Exception e){
           e.printStackTrace();
       }
        return resultado;
    }
}
