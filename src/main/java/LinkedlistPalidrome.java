class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
public class LinkedlistPalidrome {
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
    public static boolean Palindrome(Node head){
        Node slow = head;
        Node fast = head;
        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        Node j = reverse(slow);
        Node  i =head;
        while(j!=null){
            if(i.val!=j.val) return false;
            i = i.next;
            j = j.next;
        }
        return true;
    }
    public static void main(String[] args) {
        
    }
}
