public class modulotest{

    public static int checkmod(int n){
        if((n*15)%16 == 1){
            return n;
        }else{
            return checkmod(n-1);
        }
    }


    public static void main(String[] args) {
        System.out.println(checkmod(20));
    }
}