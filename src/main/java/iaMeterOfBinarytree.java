class  Node{
    int val;
    Node left,right;
    Node(int val){
        this.val = val;
        this.left=right = null;
    }
}
public class iaMeterOfBinarytree {
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
       if(root==null) return -1;
       return 1+Math.max(level(root.left), level(root.right));
    }
    public static int diameter(Node root){
       if(root==null) return -1;
       int myDia = level(root.left)+level(root.right);
       int leftDia = diameter(root.left);
       int rightDia = diameter(root.right);
       return Math.max(myDia, Math.max(leftDia, rightDia));
    }
}
