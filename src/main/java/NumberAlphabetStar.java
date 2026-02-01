
import java.util.Scanner;

public class NumberAlphabetStar {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter number");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if(j%2==0){
                    System.out.print(i+"");
                }
                else{
                    System.out.print((char)(i+64)+" ");
                }
            }
            System.out.println(" ");
        }

    }
}
