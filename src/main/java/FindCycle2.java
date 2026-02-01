 class Node {
    int val;
    Node next;
    Node(int val ){
        this.val = val;
    }
}
public class FindCycle2 {
    public static Node cycle(Node head){
        Node slow = head;
        Node fast = head;
        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(fast==slow) break;
        }
        if(fast!=slow) return null;
        Node temp = head;
        while(temp!= slow){
            temp = temp.next;
            slow = slow.next;
        }
        return slow;
    }
    public static void main(String[] args) {
        
    }
}
