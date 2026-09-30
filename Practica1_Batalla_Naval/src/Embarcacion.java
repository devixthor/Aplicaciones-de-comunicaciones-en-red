import java.util.ArrayList;
import java.util.List;

public class Embarcacion {
    private String nombre;
    private List<int[]> coordenadas;
    private int impactos;

    public Embarcacion(String nombre) {
        this.nombre = nombre;
        this.coordenadas = new ArrayList<>();
        this.impactos = 0;
    }

    public void agregarCoordenadas(int fila, int columna){
        coordenadas.add(new int[]{fila, columna});
    }

    public void imprimirCoordenadas(){
        System.out.print("Coordenadas de " + nombre + ": ");
        for(int[] coord: coordenadas){
            System.out.print("(" + coord[0] + ", " + coord[1] + ")");
        }
        System.out.println();
    }

    public boolean recibioImpacto(int fila, int columna){
        boolean impactoRecibido = false;
        for(int[] coord: coordenadas){
            if(coord[0] == fila && coord[1] == columna){
                impactos++;
                impactoRecibido = true;
            }
        }
        return impactoRecibido;
    }

    public boolean esHundido(){
        return impactos == coordenadas.size();
    }

}
