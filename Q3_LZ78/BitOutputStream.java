package Q3_LZ78; /**
 * Created by Lingwei Meng on 2018/2/12.
 */
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends FilterOutputStream {
    class BitManager {
        int buf = 0;
        int cnt = 0;

        int writeOne(int next) {
            int ret = -1;
            buf = buf * 2 + next;
            cnt++;
            if (cnt == 7) {
                cnt = 0;
                ret = buf;
                buf = 0;
            } else {
                ret = -1;
            }
            return ret;
        }

        //
        int writeLast() {
            int x = 0;
            for (int i = 0; i < 7 - cnt; ++i)
                x = x * 2 + 1;
            for (int i = 7 - cnt; i < 8; ++i)
                x = x * 2;
            return buf | x;
        }
    }

    BitManager bitManager = new BitManager();

    public BitOutputStream(OutputStream os) {
        super(os);
    }

    public void write(int i) throws IOException {
        int x = bitManager.writeOne(i >= 1 ? 1 : 0);
        if (x >= 0)
            out.write(x);
    }

    public void write(byte[] arr) throws IOException {
        write(arr, 0, arr.length);
    }

    public void write(byte[] arr, int off, int len) throws IOException {
        int clen = 0;
        for (int i = 0; i < len; ++i) {
            int x = bitManager.writeOne(arr[off + i]);
            if (x >= 0)
                arr[off + (clen++)] = (byte) x;
        }
        out.write(arr, off, clen);
    }

    public void flush() throws IOException {
        out.write(bitManager.writeLast());
        super.flush();
    }
}