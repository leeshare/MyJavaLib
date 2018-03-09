package org.lixl.maths;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/8/2018.
 *
 * int
 */
public class BigIntOperate {

    static class TenBillion{

        int signal;
        List<Integer> num = new ArrayList<Integer>();

        public TenBillion(int signal, List<Integer> num){
            this.signal = signal;
            this.num = num;
        }

        public TenBillion Add(TenBillion other){
            List<Integer> num2 = other.num;
            List<Integer> result = new ArrayList<Integer>();
            int size = Math.max(this.num.size(), num2.size());
            for(int i = 0; i < size; i++){
                int temp = 0;
                if(num2.get(i) != null){
                    temp = num2.get(i);
                }
                if(this.num.get(i) != null){
                    temp += this.num.get(i);
                }
                result.add(i, temp);
            }

            TenBillion r = new TenBillion(1, result);
            return r;
        }
        public TenBillion Sub(TenBillion other){

            return null;
        }
        public TenBillion Multiply(TenBillion other){

            return null;
        }
        public TenBillion Divide(TenBillion other){

            return null;
        }
    }

    static void printTenBillion(TenBillion tb){
        StringBuilder sb = new StringBuilder();
        for(int i = tb.num.size() - 1; i >= 0; i--){
            int temp = tb.num.get(i);
            if(i == 0){
                int zeroNum = 9 - String.valueOf(temp).length();
                for(int t = 0; t < zeroNum; t++){
                    sb.append("0");
                }
            }
            sb.append(temp);
        }
        char[] a = sb.toString().toCharArray();
        for(int i = 0; i < a.length; i++){
            if((a.length - i)%3 == 0 && i > 0){
                System.out.print(",");
            }
            System.out.print(a[i]);
        }
        System.out.print("\n");
    }

    public static void main(String[] args){

        // 305, 000, 002, 000
        List<Integer> num = new ArrayList<Integer>();
        num.add(2000);  //最高到 亿
        num.add(305);   // 305  单位 十亿
        TenBillion b = new TenBillion(1, num);
        System.out.println("第一个数：");
        printTenBillion(b);

        // 5, 000, 058, 000
        List<Integer> num2 = new ArrayList<Integer>();
        num2.add(58000);
        num2.add(5);
        TenBillion other = new TenBillion(1, num2);
        System.out.println("第二个数：");
        printTenBillion(other);

        TenBillion result = b.Add(other);
        System.out.println("结果：");
        printTenBillion(result);
    }
}
