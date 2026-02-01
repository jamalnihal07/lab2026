import java.util.Scanner;

public class BinarySearch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[5];
        int n = arr.length;
        int lb = n;
        int target = sc.nextInt();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        int lo = 0, hi = arr.length-1;
        int mid = lo + (hi-lo)/2;
        while (lo<=hi){
            if(arr[mid]>=target){
                lb = Math.min(lb, mid);
                hi = mid-1;
            }
            else lo = mid+1;
        }
    }
}
