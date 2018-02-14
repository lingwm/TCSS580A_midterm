package Q5_Calculate;

import java.text.DecimalFormat;

/**
 * Created by Lingwei Meng on 2018/2/12.
 */
public class LogCalculate {

    /**
     * Question 5
     * @return
     */
    public void calculate(){
        double result=.0;
        for (int n=1;n<=12367;n++){
            result+=(0.1/n)*log2(n/0.1);
        }
        DecimalFormat df = new DecimalFormat("0.000");
        System.out.println(df.format(result));
    }

    public double log2(double a){
        double result=Math.log(a)/Math.log(2);
        return result;
    }
}
