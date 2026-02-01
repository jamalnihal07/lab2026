class  Node{
    int val;
    Node left, right;
    Node(int val){
        this.val = val;
        this.left = right = null;
    }
}
public class SearchInBST {
    public static void main(String[] args) {
        Node a = new Node(10);
        Node b = new Node(20);
        Node c = new Node(30);
        Node d = new Node(40);
        Node e = new Node(50);
        a.left = b; a.right = c;
        b.left = d; b.right = e;
    }
    public static void print(Node root){
        if(root==null) return;
        System.out.println(root.val);
        print(root.left);
        print(root.right);
    }
    public static Node search(Node root,int val){
        if(root==null) return null;
        if(root.val<val) search(root.right, val);
        else if(root.val>val) search(root.left, val);
        return root;
    }
}
