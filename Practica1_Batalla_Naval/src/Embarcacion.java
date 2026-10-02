import com.fasterxml.jackson.databind.JsonNode;

import java.io.DataInputStream;
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

    public static Embarcacion colocarEmbarcacion(String nombre, String simbolo, int x1, int x2, int y1, int y2, Tablero tablero){
        int i, inicio, fin;
        Embarcacion tipoEmbarcacion = new Embarcacion(nombre);
        if(x1 == x2){
            inicio = Math.min(y1, y2);
            fin = Math.max(y1, y2);
            for(i = inicio; i <= fin; i++){
                tipoEmbarcacion.agregarCoordenadas(x1, i);
                tablero.registrarTiro(x1, i, simbolo);
            }
        }
        else if (y1 == y2){
            inicio = Math.min(x1, x2);
            fin = Math.max(x1, x2);
            for(i = inicio; i<= fin; i++){
                tipoEmbarcacion.agregarCoordenadas(i, y1);
                tablero.registrarTiro(i, y1, simbolo);
            }
        }
        return tipoEmbarcacion;
    }

    public static Embarcacion agregarEmbarcacionLista (String nombre, int x1, int x2, int y1, int y2){
        int i, inicio, fin;
        Embarcacion tipoEmbarcacion = new Embarcacion(nombre);
        if(x1 == x2){
            inicio = Math.min(y1, y2);
            fin = Math.max(y1, y2);
            for(i = inicio; i <= fin; i++) tipoEmbarcacion.agregarCoordenadas(x1, i);
        }
        else if (y1 == y2){
            inicio = Math.min(x1, x2);
            fin = Math.max(x1, x2);
            for(i = inicio; i<= fin; i++) tipoEmbarcacion.agregarCoordenadas(i, y1);
        }
        return tipoEmbarcacion;
    }


}




