
import java.util.LinkedList;
import java.util.Queue;

class Node {
    int val;
    Node left , right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class LevelOrderTraversalBinarytree {
    public static void main(String[] args) {
         Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
    }
    public static void print(Node root){
        if(root==null) return;
        System.out.println(root.val);
        print(root.left);
        print(root.right);
    }
    public static void lvl(Node root){
        Queue<Node> q = new LinkedList<>();
        if(root!=null) q.add(root);
        while (!q.isEmpty()){
            Node front = q.remove();
            System.out.println(front.val);
            if(front.left!=null) q.add(front.left);
            if(front.right!=null) q.add(front.right);
        }
    }
}
