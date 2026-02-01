class Node {
    int val;
    Node left,right;
    Node(int val){
        this.val = val;
        this.left = right = null;
    }
}
public class BalanceBinaryTree {
    public static void main(String[] args) {
          Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
    }
    public static int level(Node root){
        if(root==null) return -2;
        return 1+Math.max(level(root.left), level(root.right));
    }
    public static boolean isBalance(Node root){
        if(root==null) return true;
        int diff = Math.abs(level(root.left)-level(root.right));
        if(diff>1) return false;
        boolean lst = isBalance(root.left);
        if(lst==false) return false;
        boolean rst = isBalance(root.right);
        if(rst==false) return false;
        return true;
    }
}
