

class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class RotateList {
    public static Node rotate(Node head,int k){
        if(head==null || head.next==null) return head;
        int n = 0;
        Node temp = head;
        while(temp!=null){
            temp = temp.next;
            n++;
        }
        Node slow = head;
        Node fast = head;
        for (int i = 0; i < k; i++) {
            fast = fast.next;
        }
        k %= n;
        if(k==0) return head;
        while(fast.next!=null){
            fast = fast.next;
            slow = slow.next;
        }
        Node neahead = slow.next;
        slow.next = null;
        fast.next = head;
        return neahead;
    }
    public static void main(String[] args) {
        
    }
}
