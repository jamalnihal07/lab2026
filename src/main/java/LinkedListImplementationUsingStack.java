class Node{
    int val;
    Node next;
    Node(int val){
        this.val = val;
    }
}
class St{
    Node head;
    int size;
    St(){
        this.head = null;
        this.size = 0;
    }

    void push(int val){
        Node temp = new Node(val);
        if(head==null)return;
        else{
            temp.next = head;
            head = temp;
            size++;
        }
    }
     void pop(){
            if(head==null) return;
            else{
                head = head.next;
                size--;
            }
        }
        int top(){
            if(head==null) return -1;
            return head.val;
        }
        boolean isEmpty(){
            if(size==0) return true;
            return false;
        }
        int size(){
            return size;
        }
        void dislpay(){
            Node temp = head;
            while(temp!=null){
                System.out.println(temp.val);
                temp = temp.next;
            }
        }
}


public class LinkedListImplementationUsingStack {
    public static void main(String[] args) {
        
    }
}
