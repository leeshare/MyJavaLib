package org.lixl.maths.arithmetic;

import java.util.Arrays;

/**
 * 快速排序 算法1
 * 一个数组,将第0个数作为轴,记其值为s.
 * 令索引i从左向右找,直到找到大于s的数;
 * 令索引j从右向左找,直到找到小于s的数;
 * 如果 i >= j ,则跳出;
 * 如果 i < j,则交换i和j的值;
 * 将轴与j交换;
 * 对轴左边进行递归;
 * 对轴右边进行递归;
 * Created by lxl on 18/7/1.
 */
public class QuickSort1 {
    public static void sort(int[] number){
        sort(number, 0, number.length - 1);
    }

    public static void sort(int[] number, int left, int right) {
        if(left < right){
            int s = number[left];

            int i = left;
            int j = right + 1;
            while(true){
                while(i + 1 < number.length && number[++i] < s);
                while(j - 1 > -1 && number[--j] > s);
                if(i >= j){
                    break;
                }
                swap(number, i, j);
            }
            number[left] = number[j];
            number[j] = s;
            sort(number, left, j - 1);
            sort(number, j + 1, right);
        }

    }

    private static void swap(int[] number, int i, int j) {
        int t;
        t = number[i];
        number[i] = number[j];
        number[j] = t;
        System.out.println(Arrays.toString(number));
    }

    public static void main(String[] args){
        int[] number = new int[]{41, 24, 76, 11, 45, 64, 21, 69, 19, 36};
        System.out.println(Arrays.toString(number));
        sort(number);
    }
}
