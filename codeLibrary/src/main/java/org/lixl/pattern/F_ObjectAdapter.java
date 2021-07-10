package org.lixl.pattern;

/**
 * Source类 和 目标接口不变
 * 仅改 Adapter类的 继承 Source 为 持有 Source
 */
public class F_ObjectAdapter {
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

    public static class Adapter implements Targetable {
        private Source source;

        public Adapter(Source source) {
            this.source = source;
        }

        @Override
        public void method1() {
            source.method1();
        }

        @Override
        public void method2() {
            System.out.println("this is the targetable method");

        }
    }


    public static void main(String[] args) {
        Source source = new Source();
        Adapter adapter = new Adapter(source);
        adapter.method1();
        adapter.method2();
    }
}
