public class TestStatic {
    public int a;

    static {
        System.out.println("父静态代码块");
    }

    public TestStatic(){
        System.out.println("父构造函数");
    }

    {
        System.out.println("父构造代码块");
    }
}

/*
class TestStaticChild extends TestStatic {
    public int i;
    public double d;

    static {
        System.out.println("子静态代码块");
    }

    public TestStaticChild(){
        System.out.println("子构造函数");
    }

    {
        System.out.println("子构造代码块");
    }


    public static void main(String[] args) {
        TestStatic t = new TestStaticChild();

    }
}
*/
