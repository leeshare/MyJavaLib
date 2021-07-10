package org.lixl.multithreading.test;

public class ProducerAndConsumer {

    public static class ValueObject {
        public static String value = "";
    }

    public static class P {
        private String lock;

        public P(String lock) {
            this.lock = lock;
        }

        public void setValue() {
            try {
                synchronized (lock) {
                    if (!ValueObject.value.equals("")) {
                        lock.wait();
                    }
                    String value = System.currentTimeMillis() + "_" + System.nanoTime();
                    System.out.println("set的值是" + value);
                    ValueObject.value = value;
                    lock.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class C {
        private String lock;

        public C(String lock) {
            this.lock = lock;
        }

        public void getValue() {
            try {
                synchronized (lock) {
                    if (ValueObject.value.equals("")) {
                        lock.wait();
                    }
                    System.out.println("get 的值是" + ValueObject.value);
                    ValueObject.value = "";
                    lock.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String lock = new String("");
        P p = new P(lock);
        C c = new C(lock);
        Thread tp = new Thread() {
            @Override
            public void run() {
                while (true) {
                    p.setValue();
                }
            }
        };
        tp.start();
        Thread tc = new Thread() {
            @Override
            public void run() {
                while (true) {
                    c.getValue();
                }
            }
        };
        tc.start();
    }
}
