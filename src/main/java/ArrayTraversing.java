
import java.util.Scanner;

public class ArrayTraversing {
    public static void print(int i, int[] arr){
        if(i==arr.length) return;
        System.out.println(arr[i]);
        print(i+1,arr);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr  = new int[7];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        print(0, arr);

    }
}
