import java.util.Scanner;

public class MazePath {
    public static int maze(int n, int m){
        if(n==1 && m==1)return 1;
        if(n==0 ||m==0) return 0;
        return maze(n-1, m) + maze(n, m-1);
    }
    public static int maze2(int row,int col, int m, int n){
        if(row==m-1 && col==n-1)return 1;
        if(row>=m || col>=n) return 0;
        return maze2(row-1, col, m, n) + maze2(row, col-1, m, n);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        System.out.println(maze(n, m));
    }
}
