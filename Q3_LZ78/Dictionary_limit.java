package Q3_LZ78;

/**
 * Created by Lingwei Meng on 2018/2/12.
 */
public class Dictionary_limit extends Dictionary {
    int maxSize;

    Dictionary_limit(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(CodeArray str) {
        if (size() < maxSize) {
            super.add(str);
        }
    }
}