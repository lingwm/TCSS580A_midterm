import Q1_Entropy.CharacterEntropy;
import Q2_Huffman.Huffman;
import Q3_LZ78.*;
import Q5_Calculate.LogCalculate;

import java.io.*;

/**
 * Created by Lingwei Meng on 2018/2/2.
 */
public class Enter {
    public static void main(String args[]) throws IOException {
        //Q1_execute();
        //Q2_execute();
        //Q3_execute();
        //Q5_execute();
    }

    private static void Q1_execute(){
        String novel=readFile("tom.txt");
        CharacterEntropy ce=new CharacterEntropy(novel);
        long startTime = System.currentTimeMillis();
        ce.singleEntropy();
        //ce.bigramsEntropy();
        long endTime = System.currentTimeMillis();
        System.out.println("Running time ：" + (endTime - startTime) + "ms");
    }

    private static void Q2_execute(){
        String novel=readFile("tom.txt");
        Huffman hm=new Huffman();
        //hm.init_singleChar(novel);//character
        hm.init_bigrams(novel);//bigrams
        long startTime = System.currentTimeMillis();
        //hm.compress(0); //character
        //hm.compress(1); //bigrams
        long endTime = System.currentTimeMillis();
        hm.decompress();
        long endTime2 = System.currentTimeMillis();
        System.out.println("Running time of compression：" + (endTime - startTime) + "ms");
        System.out.println("Running time of decompression：" + (endTime2 - endTime) + "ms");
    }

    private static void Q3_execute(){
        LZ78 lz78 = new LZ78();

        InputStream is, isDecompress;
        OutputStream os, osDecompress;

        try {
            is = new FileInputStream("Tom.txt");
            os = new FileOutputStream("Tom.lz78");
            long startTime = System.currentTimeMillis();
            lz78.compress(is, os);

            long compressEnd = System.currentTimeMillis();

            isDecompress = new FileInputStream("Tom.lz78");
            osDecompress = new FileOutputStream("Tom_decompressed.txt");
            lz78.decompress(isDecompress, osDecompress);
            long endTime = System.currentTimeMillis();

            System.out.println("Running time of compression：" + (compressEnd - startTime) + "ms");
            System.out.println("Running time of decompression：" + (endTime - compressEnd) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Q5_execute(){
        LogCalculate lc=new LogCalculate();
        lc.calculate();
    }

    /**
     * read file
     * @return string of file content
     */
    private static String readFile(String filename){
        File tom=new File(filename);
        BufferedReader reader=null;
        String novel="";
        String temp;
        try {
            reader=new BufferedReader(new FileReader(tom));
            while ((temp=reader.readLine())!=null){
                novel+=temp;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return novel;
    }


}
