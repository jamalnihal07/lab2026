public class permutation {
    public static void permute(String ans, String s){
        if(s.length()==0){
            System.out.println(ans);
            return;
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            String left = s.substring(0,i);
            String right = s.substring(i+1);
            permute(ans+ch, left+right);
        }
    }
    public static void main(String[] args) {
        String s = "abcd";
        permute("s", s);
    }
}
