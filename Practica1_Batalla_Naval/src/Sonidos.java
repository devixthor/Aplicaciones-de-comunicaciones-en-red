import javax.sound.sampled.*;
import java.io.File;

public class Sonidos {

    private static Clip musicaFondo;

    public static void reproducir(String archivo) {
        try {
            AudioInputStream audio =  AudioSystem.getAudioInputStream(new File(archivo));

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido.");
        }
    }
    public static void sonidoFondo(String archivo) {
        detenerFondo();
        try {

            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(archivo));

            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audio);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFondo.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la música.");
            e.printStackTrace();
        }
    }
    public static void detenerFondo() {
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo.close();
            musicaFondo = null;
        }
    }
}