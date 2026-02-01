class Node{
    int val;
    Node left,right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class SameBinaryTree {
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
    public static boolean  issame(Node a, Node b){
        if(a==null && b==null) return true;
        if(a==null || b==null) return false;
        if(a.val !=b.val) return false;
        if(issame(a.left, b.left)==false) return false;
        if(issame(a.right, b.right)==false) return false;
        return true;
    }

}
