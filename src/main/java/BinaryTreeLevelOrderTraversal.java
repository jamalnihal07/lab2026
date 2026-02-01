
import java.util.List;

class Node{
    int val;
    Node left, right;
    Node(int val){
        this.val = val;
        this.left = right = null;
    }
}
public class BinaryTreeLevelOrderTraversal {
    public static void main(String[] args) {
          Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
    }
    public static void nth(Node root, int n, int lvl ,List<Integer> arr){
        if(root==null) return;;
        if(n==lvl) arr.add(root.val);
        nth(root.left, n+1, lvl, arr);
        nth(root.right, n+1, lvl, arr);
    }
    public static int level(Node root){
        if(root==null) return -1;
        return Math.max(level(root.left), level(root.right));
        
    }
}
