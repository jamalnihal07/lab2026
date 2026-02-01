
public class RemoveNthNodeFromEndOfList {
    class Node{
    int val;
    Node next;
 Node (int val){
    this.val = val;
 }
}
    public static Node removeNthFromEnd(Node head, int n){
        int len = 0;
        Node temp = head;
        while(temp!=null){
            temp = temp.next;
            len++;
        }
        temp = head;
        for (int i = 0; i < len-n-1; i++) {
            temp = temp.next;
        }
        temp.next = temp.next.next;
        return head;
    }
    public static Node remove(Node head, int n){
        Node slow = head;
        Node fast = head;
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        if(fast==null){
            return head.next;
        }
        while(fast.next!=null){
            slow = slow.next;
            fast = fast.next;
        }
        slow.next = slow.next.next;
        return head;
    }
}
