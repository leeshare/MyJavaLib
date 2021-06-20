package org.lixl.pattern.Factory;

public class PhoneFactoryTest {

    public static void main(String[] args) {
        Phone iphone = SimpleFactory.choosePhone(1);
        iphone.call();
        Phone android = SimpleFactory.choosePhone(2);
        android.call();
    }

    static class SimpleFactory {
        public static Phone choosePhone(int type) {
            switch (type){
                case 1:
                    return new IPhone();
                case 2:
                    return new Android();
            }
            return null;
        }
    }

    public interface Phone {
        public void call();
        public String os();
    }

    public static class IPhone implements Phone {

        @Override
        public void call() {
            System.out.println("tel with iphone");
        }

        @Override
        public String os() {
            return "iphone";
        }
    }

    public static class Android implements Phone {

        @Override
        public void call() {
            System.out.println("tel with android");
        }

        @Override
        public String os() {
            return "android";
        }
    }
}
