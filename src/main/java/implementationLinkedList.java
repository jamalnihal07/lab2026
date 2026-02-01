class ll{
    Node head;
    Node tail;
    int size;
     void insertAthead(int val){
        Node temp = new Node(val);
        if(head==null) head = tail = temp;
        else{
            temp.next = head;
            head = temp;
        }
        size++;
     }
     void insertAttail(int val){
        Node temp = new Node(val);
            if(head==null) head = tail = temp;
            else{
                tail.next = temp;
                tail = temp;
            }
            size++;
     }
     void insert(int idx, int val){
        Node temp = new Node(val);
        if(idx==0)insertAthead(val);
        if(idx==size)insertAttail(val);
        if(idx>size || idx<0) return;
        else{
            Node x = head;
            for (int i = 0; i < idx-1; i++) {
                x = x.next;
            }
            temp.next = x.next;
            x.next = temp;
            size++;
        } 
     }
     int get(int idx){
        if(idx==size-1) return tail.val;
        if(idx>size || idx<0) return -1;
        Node temp = head;
        for (int i = 0; i < idx; i++) {
            temp = temp.next;
        }
        return temp.val;
     }
     void display(){
        Node temp = head;
        while(temp!=null){
            System.out.println(temp.val+" ");
            temp = temp.next;
        }
     }
     void deleteAtHead(){
        if(head == null) return;;
        head = head.next;
        size--;
     }
     void deleteAtTail(){
        if(head==null) return;
        else{
            Node temp = head;
            for (int i = 0; i < size-1; i++) {
                temp = temp.next;
            }
            temp.next = null;
            tail = temp;
            size--;
        }
     }
     void delete(int idx){
        if(idx==0) deleteAtHead();
        if(idx==size-1) deleteAtTail();
        if(idx>size || idx<0) return;
        else{
            Node temp = head;
            for (int i = 0; i < idx-1; i++) {
                temp = temp.next;
            }
            temp.next = temp.next.next;
            size--;
        }
     }
     int size(){
        return size;
     }
}

public class implementationLinkedList {
    public static void main(String[] args) {
        ll list = new ll();
        list.insertAthead(20);
        list.insertAttail(10);
        list.display();
    }
}
