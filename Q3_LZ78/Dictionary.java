package Q3_LZ78; /**
 * Created by Lingwei Meng on 2018/2/12.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    Map<CodeArray, Integer> mp = new HashMap<CodeArray, Integer>();
    List<CodeArray> ls = new ArrayList<CodeArray>();

    public void add(CodeArray str) {
        mp.put(str, new Integer(ls.size()));
        ls.add(str);
    }

    public final int numFromStr(CodeArray str) {
        return (mp.containsKey(str) ? ((Integer) mp.get(str)).intValue() : -1);
    }

    public final CodeArray strFromNum(int i) {
        return (i < ls.size() ? (CodeArray) ls.get(i) : null);
    }

    public final int size() {
        return ls.size();
    }
};