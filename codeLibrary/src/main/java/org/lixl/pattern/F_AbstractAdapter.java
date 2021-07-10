package org.lixl.pattern;

/**
 * 适配器
 */
public class F_AbstractAdapter {
    public interface SourceAble {
        void method1();

        void method2();
    }

    public static abstract class Wrapper implements SourceAble {
        public void method1() {
        }

        public void method2() {
        }
    }

    public static class SourceSub1 extends Wrapper {
        @Override
        public void method1() {
            System.out.println("the sourceable interface's first Sub1");
        }
    }

    public static class SourceSub2 extends Wrapper {
        @Override
        public void method2() {
            System.out.println("the sourceable interface's second Sub2");
        }
    }

    public static void main(String[] args) {
        SourceSub1 source1 = new SourceSub1();
        SourceSub2 source2 = new SourceSub2();

        source1.method1();
        source1.method2();
        source2.method1();
        source2.method2();
    }
}
