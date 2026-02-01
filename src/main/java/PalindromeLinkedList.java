

class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class PalindromeLinkedList {
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
    public static boolean  palindrome(Node head){
        if( head.next == null) return true;
        Node newHead = new Node(head.val);
        Node t1 = head.next;
        Node t2 = newHead;
        while(t1!=null){
            Node temp = new Node(t1.val);
            t2.next = temp;
            t2 = t2.next;
            t1 = t1.next;
        }
        newHead = reverse(newHead);
        t1 = head;
        t2 = newHead;
        while(t1!=null){
            if(t1.val!=t2.val) return false;
            t1 = t1.next;
            t2 = t2.next;
        }
        return true;
    }
    public static void main(String[] args) {
        
    }
}
