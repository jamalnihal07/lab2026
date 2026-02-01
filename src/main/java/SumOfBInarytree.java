class Node{
    int val ;
    Node left, right;
    Node (int val){
        this.val = val;
        left = right = null;
    }
}
public class SumOfBInarytree {
    public static void main(String[] args) {
        Node a = new Node(10);
        Node b = new Node(20);
        Node c= new Node(30);
        Node d = new Node(40);
        Node e = new Node(50);
        a.left = b;a.right = c;
        b.left = d;b.right = e;
        System.out.println(sum(a));
    }
    public static int sum(Node root){
        if(root==null) return -1;
        return root.val +sum(root.left)+sum(root.right);
        
    }
}
