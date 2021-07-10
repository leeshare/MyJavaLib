package org.lixl.pattern;

/**
 * 桥接模式————将抽象化和实现化解耦，使二者可以独立变化。
 */
public class J_Bridge {
    public interface Sourceable {
        public void method();
    }

    public static class SourceSub1 implements Sourceable {

        @Override
        public void method() {
            System.out.println("this is the first sub");
        }
    }

    public static class SourceSub2 implements Sourceable {

        @Override
        public void method() {
            System.out.println("this is the second sub");
        }
    }

    public static abstract class Bridge {
        private Sourceable source;

        public void method() {
            source.method();
        }

        public Sourceable getSource() {
            return source;
        }

        public void setSource(Sourceable source) {
            this.source = source;
        }
    }

    public static class MyBridge extends Bridge {
        public void method() {
            getSource().method();
        }
    }

    public static void main(String[] args) {
        Bridge bridge = new MyBridge();

        Sourceable source1 = new SourceSub1();
        bridge.setSource(source1);
        bridge.method();

        Sourceable source2 = new SourceSub2();
        bridge.setSource(source2);
        bridge.method();
    }
}
