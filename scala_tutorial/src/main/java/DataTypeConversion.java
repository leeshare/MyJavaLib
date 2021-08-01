/**
 * 这是一道经典的java面试
 *      如果没有参数是byte类型，那么会调用哪个方法呢？
 *
 *      原则：低精度提升到高精度的 隐式类型转换
 *
 *      byte -> short -> int -> long   没有对应类型时的提升过程
 *      char -> int -> long    没有对应类型的提升过程
 *
 */
public class DataTypeConversion {
    public static void main(String[] args) {
        byte b = 10;
        test(b);

        char c = 'a';
        test(c);
        short c2 = (short)c;        //强转
        test(c2);

    }

    /*public static void test(byte b) {
        System.out.println("bbbb");
    }*/
    public static void test(short s) {
        System.out.println("ssss");
    }

    /**
     * char是独立的（两个字节），和byte short int long 不是同一个体系。
     * @param c
     */
    /*public static void test(char c) {
        System.out.println("cccc");
    }*/

    /**
     * 四个字节的int
     * @param i
     */
    /*public static void test(int i) {
        System.out.println("iiii");
    }*/
    public static void test(long i) {
        System.out.println("lll");
    }
}
