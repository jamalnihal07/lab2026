class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class ReOrderLinkedList {
    public static Node middle(Node head){
        Node slow = head;
        Node fast = head;
        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    public static void main(String[] args) {
        
    }
}
