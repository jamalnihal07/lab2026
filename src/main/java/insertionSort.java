
import java.util.Scanner;

public class insertionSort {
    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[5];
        System.out.println("Enter array elements");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 1; j--) {
                if(arr[j]<arr[j-1])
                    swap(arr, j, j-1);
                    else break;
            }
        }
        for(int ele : arr){
            System.out.println(ele);
        }
    }
}
