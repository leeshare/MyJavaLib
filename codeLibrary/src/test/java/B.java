/**
 * Created by lxl on 19/8/9.
 * 类编译时，类先初始化 静态域中的静态变量和 静态块，且谁在前谁先初始化
 *      其中 t1 和 t2 就是静态变量；
 */
public class B {
    public static B t1 = new B();
    {
        System.out.println("构造块");
    }
    public static void hello(){
        System.out.println("静态方法");
    }
    static {
        System.out.println("静态块");
    }
    public static B t2 = new B();
    public static void main(String[] args){
        B t = new B();
    }
}
