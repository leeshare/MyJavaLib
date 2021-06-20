/**
 * Created by Administrator on 8/13/2019.
 * 1. 类加载顺序：
 *      1)父类静态代码域（包括静态初始块，静态属性，但不包括静态方法）
        2)子类静态代码域（包括静态初始块，静态属性，但不包括静态方法）
        3)父类非静态代码域（包括非静态初始块，非静态属性）
        4)父类构造函数
        5)子类非静态代码域
        6)子类构造函数
   2. 外部类 与 静态内部类 并无直接关系，只不过调用时 需要 B2.Sub  仅此而已
 */
public class B2 {
    private String baseName = "_baseName";
    public B2(){
        callName();
    }
    public void callName(){
        System.out.println(baseName);
    }

    static class Sub extends B2{
        public String baseName = "_subName";

        //这个把父类给重写了，所以 new Sub() 在执行父类构造时，会调用 自己的 callName
        public void callName(){
            System.out.println(baseName);
        }
        /*public Sub(){
            callName();
        }*/
    }

    public static void main(String[] args){
        B2 b = new Sub();

    }
}
