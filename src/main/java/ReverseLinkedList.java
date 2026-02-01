class Node {
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class ReverseLinkedList {
    public static Node reverse(Node head){
        Node curr = head;
        Node Next = head;
        Node prev = null;
        while(curr!=null){
            Next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = Next;
        }
        return prev;
    }
    public static Node reverse2(Node head){
        if(head==null || head.next==null) return head;
        Node a = head;
        Node neaHead = reverse2(a);
        a.next = head;
        head.next = null;
        return neaHead;
    }
    public static void main(String[] args) {
        
    }
}
