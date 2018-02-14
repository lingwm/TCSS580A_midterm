package Q2_Huffman;

/**
 * Created by Lingwei Meng on 2018/2/11.
 */
public class HuffmanTree {
    private Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}

 class Node implements Comparable<Node> {
    private String chars = "";
    private int frequency = 0;
    private Node parent;
    private Node leftNode;
    private Node rightNode;

    @Override
    public int compareTo(Node n) {
        return frequency - n.frequency;
    }

    public boolean isLeaf() {
        return chars.length() == 1;
    }

    public boolean isLeaf_bigrams() {
         return chars.length() == 2;
     }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeftChild() {
        return parent != null && this == parent.leftNode;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

}
