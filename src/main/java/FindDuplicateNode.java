class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class FindDuplicateNode {
    public static Node find(Node head){
        if(head==null) return  head;
        Node a = head;
        Node b = head;
        while(b!=null){
            if(b.val==a.val) b = b.next;
            else{
                a.next = b;
                a = b;

            }
        }
        a.next = null;
        return  head;
    }
    public static void main(String[] args) {
        
    }
}
