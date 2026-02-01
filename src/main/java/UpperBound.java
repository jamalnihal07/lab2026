
import java.util.Scanner;

public class UpperBound {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[5];
        int n = arr.length;
        int ub = n;
        int target = sc.nextInt();
        int lo = 0, hi = n-1;
        while(lo<=hi){
            int mid = lo + (lo+hi-lo)/2;
            if(arr[mid]>target){
                ub = Math.min(ub, mid);
                hi = mid-1;
            }
            else lo = mid+1;
        }
    }
}
