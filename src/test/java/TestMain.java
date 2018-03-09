/**
 * Created by Administrator on 3/8/2018.
 */
public class TestMain {
    public static void main(String[] args){
        // [ << 1 乘以 2 ][ << 2 乘以 4 ]
        // 2 * 8 的值
        int a = 2 << 3;
        System.out.println(a);

        int b = 3 << 1;
        System.out.println(b);

        int c = 2 >> 1;
        System.out.println(c);

        Thread thread = new Thread();
        thread.run();

    }
}
