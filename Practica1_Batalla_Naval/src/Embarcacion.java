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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<int[]> getCoordenadas() {
        return coordenadas;
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
                tablero.registrarSimbolo(x1, i, simbolo);
            }
        }
        else if (y1 == y2){
            inicio = Math.min(x1, x2);
            fin = Math.max(x1, x2);
            for(i = inicio; i<= fin; i++){
                tipoEmbarcacion.agregarCoordenadas(i, y1);
                tablero.registrarSimbolo(i, y1, simbolo);
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

    static boolean validaciones(int x1, int y1, int x2, int y2, int longitud, String nombre, boolean ocupado[][]) {

        if (x1 < 0 || x1 >= 10 || x2 < 0 || x2 >= 10 || y1 < 0 || y1 >= 10 || y2 < 0 || y2 >= 10) {
            System.out.println("Fuera de rango del tablero.");
            return false;
        }

        if (x1 != x2 && y1 != y2) {
            System.out.println("El barco no puede estar en diagonal.");
            return false;
        }

        if (Math.abs(x2 - x1) + Math.abs(y2 - y1) != longitud - 1) {
            System.out.println("Longitud incorrecta, se deben ocupar " + longitud + " casillas.");
            return false;
        }

        int pasoX = Integer.compare(x2, x1);
        int pasoY = Integer.compare(y2, y1);
        int fila = x1;
        int columna = y1;

        for (int i = 0; i < longitud; i++) {
            if (ocupado[fila][columna]) {
                System.out.println("La casilla ya está ocupada por otro barco.");
                return false;
            }
            fila += pasoX;
            columna += pasoY;
        }
        return true;
    }

    static boolean validacionesServidor(int x1, int y1, int x2, int y2, int longitud, String nombre, boolean ocupado[][]) {

        if (x1 < 0 || x1 >= 10 || x2 < 0 || x2 >= 10 || y1 < 0 || y1 >= 10 || y2 < 0 || y2 >= 10) {
            return false;
        }

        if (x1 != x2 && y1 != y2) {
            return false;
        }

        if (Math.abs(x2 - x1) + Math.abs(y2 - y1) != longitud - 1) {
            return false;
        }

        int pasoX = Integer.compare(x2, x1);
        int pasoY = Integer.compare(y2, y1);
        int fila = x1;
        int columna = y1;

        for (int i = 0; i < longitud; i++) {
            if (ocupado[fila][columna]) {
                return false;
            }
            fila += pasoX;
            columna += pasoY;
        }
        return true;
    }

    static void ocuparBarco(int x1, int y1, int x2, int y2, int longitud, boolean ocupado[][]) {

        int pasoX = Integer.compare(x2, x1);
        int pasoY = Integer.compare(y2, y1);
        int fila = x1;
        int columna = y1;

        for (int i = 0; i < longitud; i++) {
            ocupado[fila][columna] = true;
            fila += pasoX;
            columna += pasoY;
        }
    }
}






