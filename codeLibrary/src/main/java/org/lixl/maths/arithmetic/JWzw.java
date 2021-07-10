package org.lixl.maths.arithmetic;

/**
 * 排序
 * Created by lxl on 18/6/29.
 */
public class JWzw {
    //插入排序
    public void insertArray(Integer[] in) {
        int tem = 0;
        int num = 0;
        int upnum = 0;
        for (int i = 0; i < in.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                num++;
                if (in[j] > in[j + 1]) {
                    tem = in[j + 1];
                    in[j + 1] = in[j];
                    in[j] = tem;
                    upnum++;
                } else {
                    break;
                }
            }
        }
        for (int i = 0; i < in.length; i++) {
            System.out.print(in[i]);
            if (i < in.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();
        System.out.println("插入排序循环次数:" + num);
        System.out.println("移动次数：" + upnum);
        System.out.print("\n\n\n");
    }

    //选择排序
    public void chooseArray(Integer[] in) {
        int tem = 0;
        int num = 0;
        int upnum = 0;
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in.length - 1; j++) {
                num++;
                if (in[j + 1] < in[j]) {
                    tem = in[j + 1];
                    in[j + 1] = in[j];
                    in[j] = tem;
                    upnum++;
                }
            }
        }
        for (int i = 0; i < in.length; i++) {
            System.out.print(in[i]);
            if (i < in.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();
        System.out.println("选择排序循环次数:" + num);
        System.out.println("移动次数：" + upnum);
        System.out.print("\n\n\n");
    }

    //冒泡排序
    public void efferArray(Integer[] in) {
        int tem = 0;
        int num = 0;
        int upnum = 0;
        for (int i = 0; i < in.length; i++) {
            for (int j = i; j < in.length - 1; j++) {
                num++;
                if (in[j + 1] < in[i]) {
                    tem = in[j + 1];
                    in[j + 1] = in[i];
                    in[i] = tem;
                    upnum++;
                }
            }
        }
        for (int i = 0; i < in.length; i++) {
            System.out.print(in[i]);
            if (i < in.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();
        System.out.println("冒泡排序循环次数:" + num);
        System.out.println("移动次数：" + upnum);
        System.out.print("\n\n\n");
    }


}
