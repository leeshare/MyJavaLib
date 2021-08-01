public class TestOperator {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);

        byte b = 10;
        b = (byte)(b + 1);
        b += 1;
        System.out.println(b);

        int x = 15;
        int y = ++x;// x++;
        System.out.println("x=" + x + ", y =" + y);

        x = 23;
        /**
         * 这里分三步：
         *      1.  temp = x
         *      2.  x++
         *      3.  x = temp;
         */
        x = x++;
        System.out.println(x);
    }
}
