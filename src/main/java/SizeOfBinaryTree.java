class Node{
    int val;
    Node left, right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class SizeOfBinaryTree {
    public static void main(String[] args) {
         Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
        System.out.println(size(a));
    }
    public static int size(Node root){
        if(root==null) return 0;
        return 1+size(root.left)+size(root.right);
    }
}
