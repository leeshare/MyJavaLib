import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import test.TestBean;

/**
 * Created by Administrator on 3/8/2018.
 */
public class TestMain {
    public static void main(String[] args){
        //testNum();
        testAop();
    }

    static void testAop(){
        //引用 Bean 配置的AOP 文件 （如果文件位于 resources目录，则可直接写名称）
        ApplicationContext bf = new ClassPathXmlApplicationContext("aspectTest.xml");
        TestBean bean = (TestBean)bf.getBean("test");
        bean.test();
    }

    static void testNum(){
        // [ << 1 乘以 2 ][ << 2 乘以 4 ]
        // 2 * 8 的值
        int a = 2 << 3;
        System.out.println(a);      //2 * 2^3 = 16

        int b = 3 << 1;
        System.out.println(b);      //3 * 2^1 = 6

        int c = 20 >> 2;
        System.out.println(c);      //20 / 2^2 = 5

        Thread thread = new Thread();
        thread.run();

        //final 关键字修饰一个变量，是引用的变量不能变，而引用的对象还是可以改变的。
        final StringBuilder sb = new StringBuilder("hello");            //我的理解：引用地址的起始地址是不变的了，但地址对应的值是可以变的
        //sb = new StringBuilder("cc");     //编译报错 Cannot assign a value to a final variable 'sb'
        sb.append(" world");
        System.out.println(sb);
        final StringBuffer buffer = new StringBuffer("hello");
        buffer.append(" world");
        System.out.println(buffer);

        // ==  vs equals
        // == 可 比较两个基本类型的值是否相等  和  比较两个对象所对应的地址的值是否相等
        //一个对象涉及两块内存：变量和对象本身两个内存，这里==是比较对象本身的内存
        //一个对象，如果没有重写从Object中继承的equals方法，那么就是用Object中的equals方法，与==效果一样。

        TestMain tm = new TestMain();
        TestMain tm2 = new TestMain();
        TestMain tm3 = new TestMain();
    }

    //
    int instanceVar = 0;
    static int staticVar = 0;
    public TestMain(){
        instanceVar++;
        staticVar++;
        System.out.println("instanceVar=" + instanceVar + " staticVar=" + staticVar);
    }

    public static void doClassSth(){
        //doInstanceSth();      不能直接访问实例方法
        TestMain tm = new TestMain();
        tm.doInstanceSth();

    }
    public void doInstanceSth(){
        doClassSth();
    }
}
