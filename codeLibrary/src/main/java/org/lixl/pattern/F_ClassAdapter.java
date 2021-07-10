package org.lixl.pattern;

/**
 * 通过继承，把Source类的功能，扩展到目标类中
 */
public class F_ClassAdapter {

    //有一个Source类，拥有一个方法，待适配
    public static class Source {
        public void method1() {
            System.out.println("this is a original method");
        }
    }

    //目标接口
    public interface Targetable {
        //与原类中的方法相同
        void method1();

        //新类的方法
        void method2();
    }

    /**
     * 通过这个 Adapter 类，把Source的功能扩展到Targetable中
     */
    public static class Adapter extends Source implements Targetable {

        @Override
        public void method2() {
            System.out.println("this is a targetable method");
        }
    }

    public static void main(String[] args) {
        Targetable target = new Adapter();
        target.method1();
        target.method2();
    }
}
