package org.lixl.pattern;

public class B_StaticFactory {


    //第一部分：产品
    public interface Sender {
        void Send();
    }

    public static class MailSender implements Sender {

        @Override
        public void Send() {
            System.out.println("这是一个Mail sender");
        }
    }

    public static class SMSSender implements Sender {

        @Override
        public void Send() {
            System.out.println("这是一个SMS sender");
        }
    }

    //第二部分 工厂
    public static class SendFactory {
        public static Sender produceMail() {
            return new MailSender();
        }

        public static Sender produceSMS() {
            return new SMSSender();
        }
    }

    public static void main(String[] args) {
        Sender sender = SendFactory.produceMail();
        sender.Send();
    }

}
