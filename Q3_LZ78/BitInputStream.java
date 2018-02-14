package Q3_LZ78; /**
 * Created by Lingwei Meng on 2018/2/12.
 */
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends FilterInputStream {

    /**
     * Constructor creates a new instance of Q3_LZ78.BitInputStream, A decarotor to
     * InputStream, via FilterInputStream
     */
    public BitInputStream(InputStream is) {
        super(is);
    }

    class BitManager {
        // Buffer to keep max of 7 bits (one byte)
        private int[] buf = new int[8];
        // Counter showing the bit number we are reading now
        private int cnt = -1;

        // If we are at the end of the stream
        boolean atTheEnd() {
            return ((buf[7] == 1) && (cnt < 0));
        }

        // Set the flag for the end of stream
        void setTheEnd() {
            buf[7] = 1;
            cnt = -1;
        }

        // No more buffer, means we need to read the next byte
        boolean noMoreBuffer() {
            return cnt < 0;
        }

        // set the buffer
        void setNext(int next) { // put the bits of the byte into the array
            for (cnt = 0; cnt < 8; ++cnt) {
                buf[cnt] = next % 2;
                next /= 2;
            }
            // if this was the last byte
            if (buf[7] == 1) {
                for (cnt = 7; cnt >= 0; cnt--)
                    if (buf[cnt] == 0)
                        break;
                cnt--;
            } else {
                cnt = 6;
            }
        }

        // get the next bit
        int getNext() {
            return buf[cnt--];
        }

        // how many left
        int left() {
            return cnt + 1;
        }

    };

    BitManager bitManager = new BitManager();

    byte[] tempBuf = null;
    int tempBufPtr = 0;
    int tempBufLen = 0;

    private int readNextByte() throws IOException {
        int val = -1;
        if (tempBufPtr == tempBufLen)
            val = super.read();
        else {
            byte b = tempBuf[tempBufPtr++];
            if ((b & 0x80) > 0)
                val = ((int) (b & 0x7F)) | 0x80;
            else
                val = b;
        }
        return val;
    }

    /**
     * Reads a single bit from the included stream. Returns either 1 or 0, and
     * at the end of stream returns -1.
     */
    public int read() throws IOException {
        // If we are already at the end, return -1
        if (bitManager.atTheEnd())
            return -1;
        // If we are in the last bit, then refill the buffer
        if (bitManager.noMoreBuffer()) {
            int i = readNextByte();
            if (i < 0)
                bitManager.setTheEnd();
            else
                bitManager.setNext(i);
            return read();
        }
        // Return the specific bit
        return bitManager.getNext();
    }

    /** Reads a list of bits given in the byte array as 0's and 1's */
    public int read(byte[] arr) throws IOException {
        return read(arr, 0, arr.length);
    }

    public int read(byte[] arr, int off, int len) throws IOException {
        int bytelen = ((len - bitManager.left()) / 7);
        tempBuf = new byte[bytelen];
        tempBufLen = in.read(tempBuf);
        tempBufPtr = 0;
        for (int i = 0; i < len; ++i) {
            int next = read();
            if (next < 0)
                return i;
            arr[off + i] = (byte) next;
        }
        return len;
    }
}