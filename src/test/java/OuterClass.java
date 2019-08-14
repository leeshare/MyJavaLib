/**
 * Created by Administrator on 8/14/2019.
 */
public class OuterClass {
    private float f = 1.0f;
    private static float sf = 2.0f;

    class InnerClass1{
        //public static float func(){ return f; }
    }
    abstract class InnerClass2{
        //抽象方法不可以有方法体
        //public abstract float func(){}
        public abstract float func();
    }
    static class InnerClass3{
        //静态方法不可以使用非静态变量
        //protected static float func(){ return f; }
        protected static float func(){ return sf; }
    }
    public class InnerClass4{
        //非静态内部类 不可以使用静态方法
        //static float func(){ return sf; }
    }


}
