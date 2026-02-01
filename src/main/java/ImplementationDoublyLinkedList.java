class  dNode{
    int val;
    dNode next;
    dNode prev;
    dNode(int val){
        this.val = val;
    }
}
class  dll{
    dNode head;
    dNode tail;
    int size;

    void display(){
        dNode temp = head;
        while(temp!=null){
            System.out.println(temp.val+" ");
            temp = temp.next;
        }
    }
    void insertAtHead(int val){
        dNode temp = new dNode(val);
        if(head==null) head = tail = temp;
        else{
            temp.next = head;
            head.prev = temp;
            head = temp;
        }
        size++;
    }
    void insertAtTail(int val){
        dNode temp = new dNode(val);
        if(head==null) head = tail = temp;
        else{
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
        size++;
    }
    void insert(int idx, int val){
        dNode temp = new dNode(val);
        if(idx==0) insertAtHead(val);
        else if(idx==size) insertAtTail(val);
        else if(idx>size || idx<0) return;
        else{
            dNode x = head;
            for (int i = 0; i < idx-1; i++) {
                x = x.next;
            }
            dNode y = x.next;
            x.next = temp; temp.prev = x;
            y.prev = temp; temp.next = y;
            size++;
        }
    }
    void deleteAtHead(){
        if(head==null) return;;
        head = head.next;
        head.prev = null;
        size--;
    }
    void deleteAtTail(){
        if(head==null) return;
        tail = tail.prev;
        tail.next = null;
        size--;
    }
    void delete(int idx){
        if(size==0) return;
        if(idx==0) deleteAtHead();
        else if(idx==size-1) deleteAtTail();
        if(idx>size || idx<0) return;
        else{
            dNode temp = head;
            for (int i = 0; i < idx; i++) {
                temp = temp.next;
            }
            temp.next = temp.next.next;
            temp = temp.prev;
            temp.prev = temp.prev.prev;
            size--;
        }
    }
}
public class ImplementationDoublyLinkedList {
    public static void main(String[] args) {
        
    }
}
