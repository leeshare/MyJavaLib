package lixl.point;

/**
 * Created by Administrator on 2/28/2018.
 */
public class TestValueOf {

    public static void main(String[] args){
        Integer a = Integer.valueOf("100");
        Integer b = Integer.valueOf("100");
        System.out.println("a==b " + (a == b));     //都从同一个 IntegerCache里取，所以 true

        Integer c = Integer.valueOf("128");
        Integer d = Integer.valueOf("128");
        System.out.println("c==d " + (c == d));     //默认IntegerCache.hight = 127，超出了就需要new新对象，所以 false

        Integer e = 100;        //自动装箱 编译时翻译为： Integer e = Integer.valueOf(100);
        int f = 100;            //自动拆箱
        System.out.println("a==e " + (a == e));
        System.out.println("a==f " + (a == f));

        Integer g = new Integer(100);
        System.out.println("a==g " + (a == g));     //g是一个新对象，所以 false

        Integer h = 128;        //自动装箱 编译时翻译为： Integer e = Integer.valueOf(128);
        System.out.println("c==h " + (c == h));
        int i = 128;            //自动拆箱
        System.out.println("h==i " + (h == i));     //只要和int比较，都会自动拆箱，所以 true
        int j = 128;
        System.out.println("c==j " + (c == j));     //只要和int比较，都会自动拆箱，所以 true

        //Integer a = 1; 装箱  new Integer(..)   使用 Integer.valueOf()
        //int a = 1; 拆箱  return int        使用 (new Integer()).intValue()

        /*if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);*/

        Boolean boo = new Boolean(true);
        Boolean boo2 = new Boolean(true);
        System.out.println("boo == boo2 " + (boo == boo2));
        boolean boo3 = true;
        System.out.println("boo == boo3 " + (boo == boo3));
    }
}
