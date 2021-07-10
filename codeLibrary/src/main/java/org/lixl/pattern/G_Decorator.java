package org.lixl.pattern;

/**
 * 装饰模式
 */
public class G_Decorator {
    public interface Sourceable {
        void method();
    }

    public static class Source implements Sourceable {

        @Override
        public void method() {
            System.out.println("the original method!");
        }
    }

    public static class Decorator implements Sourceable {
        private Sourceable source;

        public Decorator(Sourceable source) {
            super();
            this.source = source;
        }

        @Override
        public void method() {
            System.out.println("before decorator!");
            source.method();
            System.out.println("after decorator!");
        }
    }

    public static void main(String[] args) {
        Sourceable source = new Source();
        Decorator decorator = new Decorator(source);
        decorator.method();
    }
}
