
import java.util.Scanner;

public class FisrtLastOccurence {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[6];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        int n = arr.length;
        int target = sc.nextInt();
        int lo = 0, hi = n-1;
        while(lo<=hi){
            int mid = lo+(hi-lo)/2;
            if(arr[mid]==target) return;
        
        else if(arr[mid]<=arr[hi]){
            if(target>arr[mid]&& target<=arr[hi]) lo = mid+1;
            else hi = mid-1;
        }
        else{
            if(target>arr[lo] && target <=arr[mid]) hi = mid-1;
            else lo = mid+1;
        }
        }
   }
}
