class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
        this.next = null;
    }
}

public class DisplayLinkedList {
    public static void main(String[] args) {
        Node a = new Node(10);
        Node b = new Node(20);
        Node c = new Node(30);
        a.next = b; b.next = c;
         Node temp = a;
    while(temp != null){
        System.out.println(temp.val);
        temp = temp.next;
    }
    }
}
