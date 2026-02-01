class Node {
    int val;
    Node next;
    Node (int val){
        this.val = val;
    }
}
public class MergeTwoSortedList {
    public static Node merge(Node head1,Node head2){
        Node dummy = new Node(100);
        Node temp = dummy;
        Node temp1 = head1;
        Node temp2 = head2;
        while(temp!=null && temp2!=null){
            if(temp1.val <temp2.val){
                temp.next = temp1;
                temp1 = temp1.next;
            }
            else {
                temp.next = temp2;
                temp2 = temp2.next;
            }
            temp = temp.next;
        }
        if(temp1==null) temp.next = temp2;
        else temp.next = temp1;
        return dummy.next; 
    }
    public static void main(String[] args) {
        
    }
}
