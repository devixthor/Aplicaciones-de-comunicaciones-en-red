import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.net.Socket;

public class Servidor {
    static void main() {
        try {
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Servidor iniciado en el puerto " + s.getLocalPort());

            for(;;){
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde " + cl.getInetAddress());

                String mensajeBienvenida = "Bienvenido a batalla naval";
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());

                dos.writeUTF(mensajeBienvenida);

                int [][] matriz = new int[10][10];

                for(int i = 0; i< matriz.length; i++){
                    for(int j = 0; j< matriz.length; j++){
                        System.out.print(matriz[i][j] + "1");
                    }
                }

                dos.flush();
                dos.close();
                cl.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
