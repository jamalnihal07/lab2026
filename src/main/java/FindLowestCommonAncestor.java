class Node{
    int val;
    Node left,right;
    Node(int val){
        this.val = val;
        left = right = null;
    }
}
public class FindLowestCommonAncestor {
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
        if(root==null) return;;
        System.out.println(root.val);
        print(root.left);
        print(root.right);
    }
       public static boolean exist(Node root, Node node){
        if(node==root) return true;
        if(root==null) return false;
        return exist(root.left, node) || exist(root.right, node);
    }
    public static Node lca(Node root, Node a, Node b){
        if(a==root || b==root) return root;
        boolean alca = exist(root.left, root);
        boolean blca = exist(root.right, root);
        if(alca==true && blca==true) lca(root.left, a, b);
        if(alca==false && blca==false) lca(root.right, a, b);
        return root;
    }
}
