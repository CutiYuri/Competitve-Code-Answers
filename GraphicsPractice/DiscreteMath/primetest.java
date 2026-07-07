public class primetest {
    
    public static String primetest(int n){
        int i = 2;

        while(n%i != 0){
            i++;
            if(i==n){
                System.err.println(n+ " is a prime number");
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.err.println(primetest(211));
    }
}