public class million{
    
    public static int div(){
        int count = 0;
        for(int i = 1000000; i < 1000000000; i++){
            if(i % 7 == 0){
                count++;
            }
        } return count;
        
    }
    
    public static void main(String[] args) {
        System.err.println(div());
    }
}