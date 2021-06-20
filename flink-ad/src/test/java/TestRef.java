/**
 * Created by lxl on 19/8/11.
 * 重点: StringBuffer不是基本类型,所以传递是引用传递,所以在被调用方法内通过append会影响a
 * 由于java只有值传递,所以传递的是引用副本的地址的值
 *      再衍生一下: java栈 -> 堆 -> 方法区
 */
public class TestRef{
    public static void main(String[] args){
        StringBuffer a = new StringBuffer("A");
        StringBuffer b = new StringBuffer("B");

        System.out.println("a.hashCode=" + a.hashCode());
        operate(a, b);
        System.out.println(a + "." + b);

        Person p = new Person("hello");
        System.out.println(p);
        p.print();
        changePerson(p);
        System.out.println(p);
        p.print();

        //我这里有个疑问?  //2019-08-11
        //为什么a的值就改变了
        //为什么p就没有改变呢?
        // a也应该传递的是栈中的值(堆中的地址)
        // p也传递的是栈中的值
        // 答:原因是 p没有改变是 被调用方法中又指向一个新地址,所以对原来没有影响;
        //           而a改变是 始终操作的是同一个地址的对象,所以对原来改变了.
        // 通过后面重新改造 不使用 p = new Person(), 而使用 p.change ,可以发现 a 和 p 是完全一致的.都把原对象修改了.
    }

    static void operate(StringBuffer x, StringBuffer y){
        x.append(y);
        y = x;

        System.out.println("x.hashCode=" + x.hashCode());
    }

    static void changePerson(Person p){
        System.out.println(p);
        p.change("world");

        p = new Person("hello2");

        System.out.println(p);
    }

}

class Person {
    String x;
    public Person(String x){
        this.x = x;
    }
    public void print(){
        System.out.println(x);
    }
    public void change(String x){
        this.x = x;
    }
}
