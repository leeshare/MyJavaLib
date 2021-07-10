package org.lixl.pattern;

public class C_AbstractFactory {
    //第一部分：产品
    public interface Sender {
        void send();
    }

    public static class MailSender implements Sender {

        @Override
        public void send() {
            System.out.println("这是一个Mail sender");
        }
    }

    public static class SMSSender implements Sender {

        @Override
        public void send() {
            System.out.println("这是一个SMS sender");
        }
    }

    //第二部分 工厂
    public interface Provider {
        Sender produce();
    }

    public static class MailFactory implements Provider {
        @Override
        public Sender produce() {
            return new MailSender();
        }
    }

    public static class SMSFactory implements Provider {
        @Override
        public Sender produce() {
            return new SMSSender();
        }
    }

    public static void main(String[] args) {
        Provider factory = new SMSFactory();
        Sender sender = factory.produce();
        sender.send();
    }
}
