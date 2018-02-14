package Q3_LZ78; /**
 * Created by Lingwei Meng on 2018/2/12.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZ78 implements Inter_LZ78 {
    boolean stopped = false;

    public class Code {
        Code(int code, int ch) {
            this.ch = ch;
            this.code = code;
        }

        int ch;
        int code;
    };

    Dictionary dict;

    byte[] buf;

    public final CodeArray emptyBA = new CodeArray();
    CodeArray w = emptyBA;

    public LZ78() {
        int numOfBits = 12;
        buf = new byte[numOfBits];
        dict = new Dictionary_limit(1 << numOfBits);
        dict.add(emptyBA);
    }

    public int encodeOneChar(int n) {
        byte c = (byte) n;
        CodeArray nw = w.conc(c);
        int code = dict.numFromStr(nw);

        if (code != -1) {
            w = nw;
            return -1;
        } else {
            dict.add(nw);
            nw = w;
            w = emptyBA;
            return dict.numFromStr(nw);
        }
    }

    public Code encodeLast() {
        if (w.size() == 0)
            return null;
        byte bt = w.getLast();
        w = w.dropLast();
        return new Code(dict.numFromStr(w), (int) bt);
    }

    public final void writeCode(OutputStream os, Code c) throws IOException {
        writeCode(os, c.ch, 8);
        writeCode(os, c.code, 12);
    }

    public final void writeCode(OutputStream os, int num, int bits) throws IOException {
        for (int i = 0; i < bits; ++i) {
            os.write(num & 1);
            num = num / 2;
        }
    }

    public final Code readCode(InputStream is) throws IOException {
        int ch = readInt(is, 8);
        if (ch < 0)
            return null;
        int cd = readInt(is, 12);
        if (cd < 0)
            return null;
        return new Code(cd, ch);
    }

    public final int readInt(InputStream is, int bits) throws IOException {
        int num = 0;
        for (int i = 0; i < bits; ++i) {
            int next = is.read();
            if (next < 0)
                return -1;
            num += next << i;
        }
        return num;
    }

    public void compress(InputStream is, OutputStream os) throws IOException {
        os = new BitOutputStream(os);
        int next;
        int code;
        while ((next = is.read()) >= 0) {
            if (stopped) {
                break;
            }
            code = encodeOneChar(next);
            if (code >= 0) {
                writeCode(os, new Code(code, next));
            }
        }
        Code c = encodeLast();
        if (c != null) {
            writeCode(os, c);
        }
        os.flush();
    }

    public CodeArray decodeOne(Code c) {
        CodeArray str = dict.strFromNum(c.code);
        dict.add(str.conc((byte) c.ch));
        return str;
    }

    @SuppressWarnings("unused")
    public void decompress(InputStream is, OutputStream os) throws IOException {
        is = new BitInputStream(is);
        CodeArray str;
        Code c;
        while ((c = readCode(is)) != null) {
            if (stopped) {
                break;
            }
            if (c == null) {
                break;
            }
            str = decodeOne(c);
            os.write(str.getBytes());
            os.write(c.ch);
        }
    }

    public void stop() {
        stopped = true;
    }
}