package org.lixl.pattern;

public class H_Proxy {
    public interface Sourceable {
        void method();
    }

    public static class Source implements Sourceable {

        @Override
        public void method() {
            System.out.println("the original method!");
        }
    }

    /**
     * 新的代理对象，将原对象封装
     */
    public static class Proxy implements Sourceable {
        private Source source;

        public Proxy() {
            this.source = new Source();
        }

        @Override
        public void method() {
            System.out.println("before proxy");
            source.method();
            System.out.println("after proxy");
        }
    }

    public static void main(String[] args) {
        Sourceable source = new Proxy();
        source.method();
    }
}
