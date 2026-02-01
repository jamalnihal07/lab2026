class Node
{
    int val;
    Node left;
    Node right;
    Node(int val){
        this.val = val;
        this.left = null;
        this.right = null;
    }
}
public class ImplementBinaryTree {
    public static void main(String[] args) {
        Node a = new Node(10);
        Node b = new Node(20);
        Node c = new Node(30);
        Node d = new Node(40);
        Node e = new Node(50);
        a.left = b; a.right = c;
        b.left = d;b.right = e;
        display(a);
    }
    public static void display(Node root){
        if(root==null) return;
        System.out.println(root.val);
        display(root.left);
        display(root.right);
    }
}
