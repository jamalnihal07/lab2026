class Node{
    int val;
    Node left,right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class PrintINOrderBinaryTree {
    public static void main(String[] args) {
         Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
        Inorder(a);
    }
    public static void Inorder(Node root){
        if(root==null) return;
        Inorder(root.left);
        System.out.println(root.val);
        Inorder(root.right);
    }
}
