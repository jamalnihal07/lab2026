
import java.util.Scanner;

public class PeakIndex {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[6];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        int n = arr.length;
        int lo = 0, hi = n-1;
        while(lo<=hi){
            int mid = lo+(hi-lo)/2;
            if(arr[mid]>arr[mid+1] && arr[mid]>arr[mid-1]) return;
            else if(arr[mid]>arr[mid-1] && arr[mid]<arr[mid+1]) lo = mid+1;
            else if(arr[mid]<arr[mid-1] && arr[mid]>arr[mid+1]) hi = mid-1;
        }
    }
}
