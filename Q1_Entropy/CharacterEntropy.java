package Q1_Entropy;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Question 1
 * Created by Lingwei Meng on 2018/2/2.
 */
public class CharacterEntropy {
    private String processedNovel;

    public CharacterEntropy(String s){
        //process data string
        String novel=s.replaceAll("[^a-zA-Z]","");    //get rid of other symbols
        novel=novel.toLowerCase();
        processedNovel=novel;
    }

    /**
     * calculate the entropy of single character
     */
    public void singleEntropy(){
        //total letters
        double total=processedNovel.length();
        System.out.println("Total letters: "+total);

        //calculate each letter
        DecimalFormat df = new DecimalFormat("0.000");    //just for concision of output
        Double frequency;   //Frequency
        double count=0;
        double H = .0;  //Entropy
        for (char ch='a';ch<='z';ch++){
            count=stringCount(processedNovel,ch);
            frequency= count / total;
            H += -(frequency * Math.log(frequency) / Math.log(2));  // H = -∑Pi*log2(Pi)
            System.out.println(ch+": "+count+" times;  frequency: "+df.format(frequency));
        }
        System.out.println("Entropy: "+H);

    }


    /**
     * count the number of substr
     * @param main
     * @param sub
     * @return count
     */
    private int stringCount(String main,char sub){
        String substr=String.valueOf(sub);
        int count = 0;
        Matcher m = Pattern.compile(substr).matcher(main);
        while (m.find()) {
            count++;
        }
        return count;
    }


    /**
     * calculate the entropy of bigrams
     */
    public void bigramsEntropy(){
        Map<String,Integer> bigramsCount=new HashMap<>(); //recording bigrams and their counts
        //calculate counts of bigrams
        for (int i=0;i<processedNovel.length()-1;i=i+2){
            char ch1=processedNovel.charAt(i);
            char ch2=processedNovel.charAt(i+1);
            String bigram=String.valueOf(String.valueOf(ch1)+String.valueOf(ch2));
            if (bigramsCount.containsKey(bigram)){
                int count=bigramsCount.get(bigram);
                bigramsCount.put(bigram,count+1);
            }else {
                bigramsCount.put(bigram,1);
            }
        }
        //calculate frequency
        Iterator it=bigramsCount.entrySet().iterator();
        int allCount=0;
        while (it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            allCount+=(int)entry.getValue();
        }
        double frequency;   //Frequency
        double H = .0;  //Entropy
        for (String key:bigramsCount.keySet()){
            frequency=((double)bigramsCount.get(key))/((double)allCount);
            H += -(frequency * Math.log(frequency) / Math.log(2));  // H = -∑Pi*log2(Pi)
        }

        System.out.println("Entropy of bigrams: "+H);
    }
}
