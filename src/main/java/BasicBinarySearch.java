
import java.util.Scanner;

public class BasicBinarySearch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int [] arr = new int[5];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }
        int target = sc.nextInt();
        int lo = 0, hi = arr.length-1;
        while (lo<=hi){
            int mid = lo+(hi-lo)/2;
            if(arr[mid]==target){
                System.out.println("Element found ");
                return;
            }
            else if(arr[mid]<target){
                lo = mid+1;
            }
            else hi = mid-1;
        }
        for(int ele : arr){
            System.out.println(ele);
        }
    }
}
