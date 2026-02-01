class Arr{
    int[] arr = new int[5];
    int idx;
    int size;
     
    Arr(){
        this.idx = -1;
        this.size = 0;
    }

    void push(int val){
        if(size==arr.length) return;
        arr[idx++] = val;
        size++;
    }
    int pop(){
        if(size==0) return-1;
        int val = arr[idx-1];
        idx--;
        size--;
        return val;
    }
    int top(){
        if(size==0) return -1;
        return arr[idx-1];
    }
    int size(){
        return size;
    }
    boolean isEmpty(){
        if(size==0) return true;
        return false;
    }
    boolean isFull(){
        if(size==arr.length) return true;
        return false;
    }
    void dislpay(){
        for (int i = 0; i< size; i++){
            System.out.println(arr[i]+" ");
        }
    }
}

public class ArrayImplementationUsingStack {
    public static void main(String[] args) {
        Arr st = new Arr();
        st.push(10);
        st.push(20);
        st.dislpay();
        

    }

}