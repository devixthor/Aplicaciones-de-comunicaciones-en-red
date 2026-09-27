public class Tablero {
    private String[][] tablero;
    private int filas;
    private int columnas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new String[filas][columnas];
        iniciarTablero();
    }

    private void iniciarTablero(){
        int i,j;
        for(i=0; i<filas; i++){
            for(j=0; j<columnas; j++){
                tablero[i][j] = "-";
            }
        }
    }

    public void registrarTiro(int f, int c, String simbolo) {
        if (f >= 0 && f < filas && c >= 0 && c < columnas) {
            tablero[f][c] = simbolo;
        }
    }

    public String actualizarTablero() {
        StringBuilder sb = new StringBuilder();
        sb.append("   0  1  2  3  4  5  6  7  8  9\n");
        for (int i = 0; i < filas; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < columnas; j++) {
                sb.append("[").append(tablero[i][j]).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}




