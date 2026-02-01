class Node{
    int val;
    Node left, right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class MaxValueInBinarytree {
    public static void main(String[] args) {
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
        System.out.println(max(a));
    }
    public static int max(Node root){
        if(root==null) return -1;
        return Math.max(root.val, Math.max(max(root.left),max(root.right)));
    }
}
