
import java.util.Scanner;

public class FindSqrt {
    public static int sqrt(int x){
        int lo = 0, hi = x;
        while(lo<=hi){
            int mid = lo+(hi-lo)/2;
            if(mid==x/mid) return mid;
            else if(mid>x/mid) hi = mid-1;
            else lo = mid+1;
        }
        return hi;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number");
        int x = sc.nextInt();
        sqrt(x);
    }
}
