class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class FindCycleInLinkedList {
    public static boolean  cycle(Node head){
        Node slow = head;
        Node fast = head;
        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(fast==slow) return true;
        }
        return false;
        
    }
    public static void main(String[] args) {
        
    }
}
