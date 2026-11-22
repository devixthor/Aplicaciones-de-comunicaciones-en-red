import java.io.*;

public interface MetodoHttp {
    void atender(String line, InputStream in, PrintWriter pw, BufferedOutputStream bos) throws Exception;
}

