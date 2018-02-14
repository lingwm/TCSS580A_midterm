package Q2_Huffman;

import java.io.*;
import java.util.*;

/**
 * Created by Lingwei Meng on 2018/2/10.
 */
public class Huffman {
    private String compress_str;  //original input
    private Map<Character, Integer> charCounts=new HashMap<>();  //times of each character
    private Map<String, Integer> bigramsCounts=new HashMap<>();  //times of bigrams
    private List<Node> leafs=new ArrayList<>();  //leaf nodes
    private HuffmanTree tree=new HuffmanTree();
    private Map<Character, String> code_table = new HashMap<>(); // code table for character
    private Map<String, String> code_table_bigrams = new HashMap<>(); // code table for bigrams

    /**
     * count times of each character
     * build Q2_Huffman.Huffman tree
     * @param s
     */
    public void init_singleChar(String s){
        compress_str=s;
        char symbols[]=s.toCharArray();
        for (char c : symbols) {
            Character character = c;
            if (charCounts.containsKey(character)) {
                charCounts.put(character, charCounts.get(character) + 1);
            } else {
                charCounts.put(character, 1);
            }
        }
        buildTree(0);
    }

    /**
     * count times of bigrams
     * build Q2_Huffman.Huffman tree
     * @param s
     */
    public void init_bigrams(String s){
        compress_str=s;
        char symbols[]=s.toCharArray();
        for (int i=0;i<symbols.length-1;i=i+2){
            String bigram=String.valueOf(symbols[i])+String.valueOf(symbols[i+1]);
            if (bigramsCounts.containsKey(bigram)) {
                bigramsCounts.put(bigram, bigramsCounts.get(bigram) + 1);
            } else {
                bigramsCounts.put(bigram, 1);
            }
        }
        buildTree(1);
    }

    /**
     * build a complete binary tree
     * the frequency of left node is always not larger than right left for every node which is not a leaf
     * @param t 0 for character, 1 for bigrams
     */
    private void buildTree(int t){
        PriorityQueue<Node> pq=new PriorityQueue<>();

        //add nodes into PriorityQueue
        if (t==0){  //for single character
            Character[] keys=charCounts.keySet().toArray(new Character[0]);
            for(Character c:keys){
                Node node=new Node();
                node.setChars(c.toString());
                node.setFrequency(charCounts.get(c));
                pq.add(node);
                leafs.add(node);
            }
        }else{  //for bigrams
            String[] keys=bigramsCounts.keySet().toArray(new String[0]);
            for(String s:keys){
                Node node=new Node();
                node.setChars(s);
                node.setFrequency(bigramsCounts.get(s));
                pq.add(node);
                leafs.add(node);
            }
        }
        //build tree
        int size=pq.size();
        for (int i=1;i<size;i++){
            Node n1=pq.poll();
            Node n2=pq.poll();
            Node merge=new Node();
            merge.setChars(n1.getChars()+n2.getChars());
            merge.setFrequency(n1.getFrequency()+n2.getFrequency());
            merge.setLeftNode(n1);
            merge.setRightNode(n2);
            n1.setParent(merge);
            n2.setParent(merge);
            pq.add(merge);
        }
        tree.setRoot(pq.poll());

        //build code table
        if (t==0){
            for (Node leaf:leafs){
                Character ch= leaf.getChars().charAt(0);
                String code="";
                Node current=leaf;
                do{
                    if (current.isLeftChild()){
                        code="0"+code;
                    }else {
                        code="1"+code;
                    }
                    current=current.getParent();
                }while (current.getParent()!=null);

                code_table.put(ch,code);
            }
        }else {
            for (Node leaf:leafs){
                String s= leaf.getChars().substring(0,2);
                String code="";
                Node current=leaf;
                do{
                    if (current.isLeftChild()){
                        code="0"+code;
                    }else {
                        code="1"+code;
                    }
                    current=current.getParent();
                }while (current.getParent()!=null);

                code_table_bigrams.put(s,code);
            }
        }
    }

    /**
     * build compressed file
     * @throws IOException
     */
    public void compress(int type) throws IOException {
        String code;
        if (type==0){
            code=encode(0); //character
        }else{
            code=encode(1);  //bigrams
        }
        FileOutputStream fos = new FileOutputStream("test.zip");
        String s;
        while (code.length()>=8){
            s=code.substring(0,8);
            int i=String_to_Int(s);
            fos.write(i);
            fos.flush();
            code=code.substring(8);
        }
        //handle the tail.(adding "0" until length==8)
        int last=8-code.length();
        for (int i = 0; i <last; i++) {
            code+="0";
        }
        s=code.substring(0, 8);
        fos.write(String_to_Int(s));
        fos.write(last); //the number of added "0"
        fos.flush();
        fos.close();
    }

    /**
     * encode
     * produce "0""1" String
     * @return
     */
    private String encode(int type){
        if (compress_str == null || compress_str.equals("")) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        char[] charArray = compress_str.toCharArray();
        if (type==0){ //character
            for (char c : charArray) {
                Character character = c;
                buffer.append(code_table.get(character));
            }
        }else {  //bigrams
            int size=charArray.length;
            for (int i=0;i<size-1;i=i+2){
                String s=String.valueOf(charArray[i])+String.valueOf(charArray[i+1]);
                buffer.append(code_table_bigrams.get(s));
            }
        }
        return buffer.toString();
    }

    /**
     * change string to int
     * @param s
     * @return
     */
    private int String_to_Int(String s){
        int v1=(s.charAt(0)-48)*128;
        int v2=(s.charAt(1)-48)*64;
        int v3=(s.charAt(2)-48)*32;
        int v4=(s.charAt(3)-48)*16;
        int v5=(s.charAt(4)-48)*8;
        int v6=(s.charAt(5)-48)*4;
        int v7=(s.charAt(6)-48)*2;
        int v8=(s.charAt(7)-48)*1;
        return v1+v2+v3+v4+v5+v6+v7+v8;
    }

    public void decompress(){
        try {
            FileInputStream fis = new FileInputStream("test.zip");
            PrintWriter pw=new PrintWriter(new File("decompress.txt"));
            String code=""; //huffman code of the novel

            //read huffman code
            while (fis.available()>1){
                code+=Int_To_String(fis.read());
            }
            //read the number of added "0"
            int i=fis.read();
            //delete "0"
            code=code.substring(0, code.length()-i);

            //decode

            //build List of chars in huffman code
            char[] charArray = code.toCharArray();
            LinkedList<Character> charList = new LinkedList<Character>();
            for (char aCharArray : charArray) {
                charList.addLast(aCharArray);
            }
            StringBuffer buffer = new StringBuffer();

            while (charList.size() > 0) {
                Node node = tree.getRoot();
                do {
                    Character c = charList.removeFirst();
                    if (c.charValue() == '0') {
                        node = node.getLeftNode();
                    } else {
                        node = node.getRightNode();
                    }
                } while (!node.isLeaf_bigrams());

                buffer.append(node.getChars());
            }
            String result=buffer.toString();
            pw.print(result);
            pw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * change int to string
     * @param value
     * @return
     */
    public String Int_To_String(int value) {
        String s="";
        for (int i = 0; i < 8; i++) {
            s=value%2+s;
            value=value/2;
        }
        return s;
    }
}
