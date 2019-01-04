package resume;

public class Adapter {

    public class Voltage220{
        public int output220V(){
            int src = 220;
            System.out.println("我是" + src + "V");
            return src;
        }
    }

    public interface IVoltage5{
        int output5V();
    }

    public class Voltage5 extends Voltage220 implements IVoltage5{
        @Override
        public int output5V(){
            int src = output220V();
            System.out.println("适配器工作开始适配电压");
            int dst = src/44;
            System.out.println("适配完，输出电压为" + dst + "V");
            return dst;
        }
    }

    public class Mobile {
        public void changing(Voltage5 voltage5) {
            if (voltage5.output5V() == 5) {
                System.out.println("电压刚刚好5V，开始充电");
            } else if (voltage5.output5V() > 5) {
                System.out.println("电压超过5V，都闪开 我要变成note7了");
            }
        }
    }

    public static void main(String[] args){
        System.out.println("===============类适配器==============");
        //Mobile mobile = new Mobile();
        //mobile.charging(new Voltage5());
    }
}
