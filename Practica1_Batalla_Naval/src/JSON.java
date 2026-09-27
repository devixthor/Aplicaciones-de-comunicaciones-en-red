import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JSON {
    static ObjectMapper objectMapper = new ObjectMapper();
    int fila;
    int columna;

    public JSON() {
    }

    public String generarJSON(int fila, int columna){
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
}
