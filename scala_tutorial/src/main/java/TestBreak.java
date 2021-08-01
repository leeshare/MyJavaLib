public class TestBreak {

    public static void main(String[] args) {
        for(int i = 0;i < 5; i++){
            if(i == 3){
                break;
            }
            System.out.println(i);
        }
        System.out.println("循环体外的代码");

        try {
            for(int i = 0; i < 5; i++){
                if(i == 3){
                    throw new RuntimeException();
                }
                System.out.println(i);
            }
        } catch (Exception e){

        }
        System.out.println("循环体外的代码");
    }
}
