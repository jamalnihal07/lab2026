
import java.util.Stack;

public class DisplayRecursivelyStack {
    public static void main(String[] args) {
        Stack<Integer> st = new Stack<>();
        st.push(10);
        st.push(20);
        st.push(30);
        st.push(40);
        st.push(50);
        display(st);

    }
    public static void display(Stack<Integer> st){
        if(st.isEmpty()) return;
        int top = st.pop();
        display(st);
        System.out.println(top);
        st.push(top);
    }
}
