package org.lixl.acm;

import java.util.Scanner;

/**
 * Created by Administrator on 2/27/2018.
 */
public class SplitOddEven {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int a = 0;

        while(a >= 0){
            try{
                a = scanner.nextInt();
            } catch(Exception e){
                e.printStackTrace();
                a = -1;
                return;
            }

            if(a < 2 || a > 10000){
                System.out.println("请输入一个2-10000的整数");
                return;
            }

            java.util.List odd = new java.util.ArrayList<Integer>();
            java.util.List even = new java.util.ArrayList<Integer>();
            for(int i = 1; i <= a; i++){
                if(i % 2 == 1){
                    odd.add(i);
                }else {
                    even.add(i);
                }
            }
            System.out.println(odd);
            System.out.println(even);
            System.out.println("\n");

        }
    }
}
