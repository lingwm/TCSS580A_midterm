package Q3_LZ78; /**
 * Created by Lingwei Meng on 2018/2/12.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Inter_LZ78 {
    public void compress(InputStream inp, OutputStream out) throws IOException;
    public void decompress(InputStream inp, OutputStream out) throws IOException;
}