package leetcode;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.Arrays;

/**
 * 反转数
 */
public class MyReverse {
    public static void main(String[] args) {
        int a = -987000;
        //a = 1534236469;
        a = -2147483648;
        int result = reverse(a);
        System.out.println(result);
        result = reverse2(a);
        System.out.println(result);
    }

    public static int reverse(int x) {
        Boolean isContainNative = x < 0;
        String strX = String.valueOf(Math.abs(x));
        if(strX.startsWith("-")){
            strX = strX.substring(1, strX.length());
        }
        while(strX.endsWith("0")){
            strX = strX.substring(0, strX.length() - 1);
            //strX = new String(strX, 0, strX.length - 1);
        }
        char[] arrX = strX.toCharArray();
        //String[] arr = new String[arrX.length];
        StringBuilder r = new StringBuilder();

        for(int i = arrX.length - 1; i >= 0; i--){
            r.append(arrX[i]);
            //arr[i] = String.valueOf( arrX[arrX.length - 1 - i] );
        }

        String strReverse = r.toString();
        int intReverse = 0;
        try {
           intReverse = Integer.parseInt(strReverse);
        } catch (Exception e) {

        }
        intReverse = isContainNative ? -intReverse : intReverse;

        return (int)intReverse;
    }

    /**
     * 一边求余，一边求商：把余作为另一个数的结尾，把商作为下次要迭代的数
     *
     * @param x
     * @return
     */
    public static int reverse2(int x) {
        long n = 0;
        while(x != 0){
            n = n * 10 + x % 10;
            x = x / 10;
        }
        return (int)n == n ? (int)n : 0;
    }

}
