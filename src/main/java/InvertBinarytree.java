class Node{
    int val;
    Node left, right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class InvertBinarytree {
    public static Node invert(Node root){
       if(root==null) return null;
       if(root.left==null && root.right==null) return root;
       Node temp = root.left;
       root.left = root.right;
       root.right = temp;
       invert(root.left);
       invert(root.right);
       return root;
    }
    public static void main(String[] args) {
     Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.left = b; a.right = c;
        b.left = d; b.right = e;        
    }
    public static void display(Node root){
        if(root==null) return;
        System.out.println(root.val);
        display(root.left);
        display(root.right);
    }
}
