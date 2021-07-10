package resume;

public class Singleton {
    private static volatile Singleton singleton;
    private Singleton(){

    }

    public static Singleton instance(){
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args){
        Singleton singleton = Singleton.instance();
    }
}
