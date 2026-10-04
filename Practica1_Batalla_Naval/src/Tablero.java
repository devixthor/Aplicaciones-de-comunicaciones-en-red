import java.util.Random;

public class Tablero {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_ROJO  = "\u001B[31m";
    private static final String ANSI_AZUL  = "\u001B[34m";
    public static final String ANSI_VERDE = "\u001B[32m";
    public static final String ANSI_AMARILLO = "\u001B[33m";

    private String[][] tablero;
    private int filas;
    private int columnas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.tablero = new String[filas][columnas];
        iniciarTablero();
    }

    private void iniciarTablero() {
        int i, j;
        for (i = 0; i < filas; i++) {
            for (j = 0; j < columnas; j++) {
                tablero[i][j] = "~";
            }
        }
    }

    public void registrarSimbolo(int f, int c, String simbolo) {
        if (f >= 0 && f < filas && c >= 0 && c < columnas) {
            tablero[f][c] = simbolo;
        }
    }

    private String colorear(String simbolo) {
        switch (simbolo) {
            case "A":
            case "S":
            case "C":
            case "D":
                return ANSI_AZUL + simbolo + ANSI_RESET;
            case "O":
                return ANSI_VERDE + simbolo + ANSI_RESET;
            case "X":
                return ANSI_ROJO + simbolo + ANSI_RESET;
            case "#":
                return  ANSI_AMARILLO + simbolo + ANSI_RESET;
                default:
                return simbolo;
        }
    }

    public String actualizarTablero() {
        StringBuilder sb = new StringBuilder();
        sb.append("   0  1  2  3  4  5  6  7  8  9\n");
        for (int i = 0; i < filas; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < columnas; j++) {
                sb.append("[").append(colorear(tablero[i][j])).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    static String sinAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    static void imprmirTableros(String t1, String t2) {
        String[] lineasT1 = t1.split("\n");
        String[] lineasT2 = t2.split("\n");

        int max = Math.max(lineasT1.length, lineasT2.length);
        for (int i = 0; i < max; i++) {
            String a = i < lineasT1.length ? lineasT1[i] : "";
            String b = i < lineasT2.length ? lineasT2[i] : "";

            String relleno = " ".repeat(Math.max(0, 35 - sinAnsi(a).length()));
            System.out.println(a + relleno + b);
        }
    }

    static Embarcacion llenarEmbarcacionAleatoria(String nombre, int longitud, Tablero tablero, String simbolo, boolean ocupado[][]) {
        Random random = new Random();
        while (true) {
            int x1 = random.nextInt(10);
            int x2 = random.nextInt(10);
            int y1 = random.nextInt(10);
            int y2 = random.nextInt(10);

            if (!Embarcacion.validacionesServidor(x1, y1, x2, y2, longitud, nombre, ocupado)) {
                continue;
            }
            Embarcacion.ocuparBarco(x1, y1, x2, y2, longitud, ocupado);
            return Embarcacion.colocarEmbarcacion(nombre, simbolo, x1, x2, y1, y2, tablero);
        }
    }
}




