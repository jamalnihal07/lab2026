class  Node{
    int val = 0;
    Node left, right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class ProductOfBinaryTree {
    public static void main(String[] args) {
        Node a = new Node(10);
        Node b = new Node(20);
        Node c = new Node(30);
        Node d = new Node(40);
        Node e = new Node(50);
        a.left = b; a.right = c;
        b.left = d;b.right  = e;
    }
    public static int prod(Node root){
        if(root==null) return -1;
        return  root.val * prod(root.right) * prod(root.left);
    }
}
