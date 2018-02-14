package Q3_LZ78;

/**
 * Created by Lingwei Meng on 2018/2/12.
 */
public class CodeArray {

    final byte[] arr;

    CodeArray(byte[] b) {
        arr = (byte[]) b.clone();
    }

    CodeArray() {
        arr = new byte[0];
    }

    CodeArray(byte b) {
        arr = new byte[] { b };
    }

    public boolean equals(Object o) {
        CodeArray ba = (CodeArray) o;
        return java.util.Arrays.equals(arr, ba.arr);
    }

    public int hashCode() {
        int code = 0;
        for (int i = 0; i < arr.length; ++i)
            code = code * 2 + arr[i];
        return code;
    }

    public int size() {
        return arr.length;
    }

    byte getAt(int i) {
        return arr[i];
    }

    public CodeArray conc(CodeArray b2) {
        int sz = size() + b2.size();
        byte[] b = new byte[sz];
        for (int i = 0; i < size(); ++i)
            b[i] = getAt(i);
        for (int i = 0; i < b2.size(); ++i)
            b[i + size()] = b2.getAt(i);
        return new CodeArray(b);
    }

    public CodeArray conc(byte b2) {
        return conc(new CodeArray(b2));
    }

    public byte[] getBytes() {
        return (byte[]) arr.clone();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public byte getLast() {
        return arr[size() - 1];
    }

    public CodeArray dropLast() {
        byte[] newarr = new byte[size() - 1];
        for (int i = 0; i < newarr.length; ++i)
            newarr[i] = arr[i];
        return new CodeArray(newarr);
    }

    public String toString() {
        return new String(arr);
    }
}