package org.lixl.pattern;

public class B_OrdinaryFactory {

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
        public Sender produce(String type) {
            if (type.equals("mail")) {
                return new MailSender();
            } else if (type.equals("sms")) {
                return new SMSSender();
            } else {
                System.out.println("未知类型");
                return null;
            }
        }
    }

    public static void main(String[] args) {
        SendFactory factory = new SendFactory();
        Sender sender = factory.produce("mail");
        sender.Send();
    }

}
