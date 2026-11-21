import java.io.*;

public interface MetodoHttp {
    void atender(String line, BufferedReader br, PrintWriter pw, BufferedOutputStream bos) throws Exception;
}

