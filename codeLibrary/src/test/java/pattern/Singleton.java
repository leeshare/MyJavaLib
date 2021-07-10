package pattern;

public class Singleton {

    //这个叫 饿汉模式：实例提前创建好，简单安全。
    private static final Singleton instance = new Singleton();

    private Singleton(){
    }

    public static Singleton getInstance(){
        return instance;
    }

    private static Object lock = new Object();
    //这个叫 懒汉模式：
    private static volatile Singleton instance2 = null;
    public static Singleton getInstance2(){
        if(instance2 == null){
            synchronized (lock){
                instance2 = new Singleton();
            }
        }
        return instance2;
    }


    public static void main(String[] args) {

        System.out.println("饿汉模式");
        for (int i = 0; i < 10; i++) {
            new Runnable(){

                @Override
                public void run() {
                    int hashCode = Singleton.getInstance().hashCode();
                    System.out.println(hashCode);

                }
            }.run();
        }

        System.out.println("懒汉模式");
        for (int i = 0; i < 10; i++) {
            new Runnable() {

                @Override
                public void run() {
                    System.out.println(Singleton.getInstance2().hashCode());
                }
            }.run();
        }

    }
}
