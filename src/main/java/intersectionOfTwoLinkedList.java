class Node{
    int val;
    Node next;
    Node (int val){
        this.val = val;
    }
}

public class intersectionOfTwoLinkedList {
    public static Node intersect(Node head1,Node head2){
        int lena = 0;
        Node temp1 = head1;
        while(temp1!=null){
            temp1 = temp1.next;
            lena++;
        }
        int lenb = 0;
        Node temp2 = head2;
        while(temp2!=null){
            temp2 = temp2.next;
            lenb++;
        }
        if(lena>lenb){
            for (int i = 0; i < lena-lenb; i++) {
                head1 = head1.next;
            }
        }
        else {
            for (int i = 0; i < lenb-lena; i++) {
                head2 = head2.next;
            }
        }
        while(temp1 != temp2){
            temp1 = temp1.next;
            temp2 = temp2.next;
        }
        return temp1;
    }
    public static void main(String[] args) {
        
    }
}
