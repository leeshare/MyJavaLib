/**
 * Created by lxl on 18/7/1.
 */
public class TestSth {
    public static void main(String[] args){
        assignment();
    }

    private static void assignment(){
        int left = 2;       //left 初始为 2
        int i = left;       //i 也赋值为 2  我理解为指向值为2的地址
        i = 10;  //i 赋值为 10   i改为指向值为10的地址,left仍指向2
        left = 5; //left指向值为5的地址

        System.out.println(String.format("left=%d, i=%d", left, i));
    }
}
