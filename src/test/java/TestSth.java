/**
 * Created by lxl on 18/7/1.
 */
public class TestSth {
    public static void main(String[] args){
        assignment();

        TestSth t = new TestSth();
        int i = 0;
        t.fermin(i);
        i = i++;
        System.out.println(i);

        byte a1, a2;
        byte b1=4, c1=5;
        final byte b2=4, c2=5;
        a1 = (byte)(b1 + c1);
        a2 = b2 + c2;
        System.out.println("a1=" + a1 + " a2=" + a2);
        System.out.println((int)' ');

        short s1 = 1;
        s1 += 1;
        System.out.println("s1=" + s1);

        i = 0;
        System.out.println((i++ + ++i));

        try {
            test(new int[] {0, 1, 2, 3, 4, 5});
        } catch (Exception e) {
            System.out.print("E");
        }
    }

    private static void assignment(){
        int left = 2;       //left 初始为 2
        int i = left;       //i 也赋值为 2  我理解为指向值为2的地址
        i = 10;  //i 赋值为 10   i改为指向值为10的地址,left仍指向2
        left = 5; //left指向值为5的地址

        System.out.println(String.format("left=%d, i=%d", left, i));

        String str = "hello";
        str += 'a';
        int len = str.length();
        //str = 100;
        str += 100;
        System.out.println(str);
    }

    void fermin(int i){
        i++;
    }


    private static void test(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            try {
                if (arr[i] % 2 == 0) {
                    throw new NullPointerException();
                } else {
                    System.out.print(i);
                }
            }
            finally {
                System.out.print("e");
            }
        }
    }

}
