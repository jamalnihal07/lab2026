class Node {
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class SortList {
    public static Node merger(Node head1,Node head2){
        Node dummy =  new Node(100);
        Node temp =  dummy;
        Node temp1 = head1;
        Node temp2 = head2;
        while(temp1 !=null && temp2 !=null){
            if(temp1.val < temp2.val){
                temp.next = temp1;
                temp1 = temp1.next;
            }
            else{
                temp.next = temp2;
                temp2 = temp2.next;
            }
            temp = temp.next;
        }
        if(temp1==null) temp.next = temp2;
        else temp.next = temp1;
        return dummy.next;
    }
    public static Node sort(Node head){
        if(head == null || head.next == null) return head;
        Node first = head;
        Node slow = head;
        Node fast = head;
        while(fast.next!=null && fast.next.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        Node second = slow.next;
        slow.next = null;
        first = sort(first);
        second = sort(second);
        return merger(first, second);
    }
    public static void main(String[] args) {
        
    }
}