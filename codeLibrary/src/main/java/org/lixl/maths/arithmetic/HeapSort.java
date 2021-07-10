package org.lixl.maths.arithmetic;

import java.util.Arrays;

/**
 * 取一个数组中最大的3个元素
 * : 小顶堆
 * 完全按我的理解写的算法:
 * 步骤1: 排序数组中前3个元素,把最小一个排最前
 * 步骤2: 遍历数组最后一个 至 数组第 3个元素, 分别和数组第0个元素比较,小于,则跳过,若大于,则交换第0个与此元素
 * 重复步骤1
 * 重复步骤2,只不过比较索引已向前走一位(index  i--)
 * Created by lxl on 18/7/7.
 */
public class HeapSort {
    final static int smallLength = 3;
    int[] s;
    //小顶堆
    //private static int[] smallHeap = new int[]{0, 0, 0};


    //元素交换
    private static void swap(int[] s, int i, int j) {
        //System.out.printf("\nswap, i=%d, s[i]=%d, j=%d, s[j]=%d", i, s[i], j, s[j]);
        System.out.println(String.format("交换 %d 和 %d", s[i], s[j]));
        int t = s[i];
        s[i] = s[j];
        s[j] = t;

        System.out.println(Arrays.toString(s));
    }

    //将数组前3个元素中,最小一个排第0位
    private static void bubbleSort(int[] s, Boolean isAllSort) {
        System.out.println("冒泡前:" + Arrays.toString(s));
        if (isAllSort) {
            for (int i = 0; i < smallLength; i++) {
                for (int j = i; j < smallLength; j++) {
                    if (s[i] > s[j]) {
                        swap(s, i, j);
                    }
                }
            }
        } else {
            int t = s[0];
            for (int i = 0; i < smallLength; i++) {
                if (t > s[i]) {
                    swap(s, 0, i);
                    t = s[0];
                }
            }
        }
        System.out.println("小顶堆排序:[" + Arrays.toString(s) + "]");
    }

    //堆排序,用数组第0个元素和 未排序的进行比较
    public static void heapSort(int[] s, int index) {
        int small = s[0];
        for (int i = s.length - 1; i >= smallLength; i--) {
            if (s[i] > small) {
                swap(s, 0, i);
                bubbleSort(s, false);
                small = s[0];
            }
            index--;
        }

    }

    public static void main(String[] args) {
        int data[] = {5, 0, 6, 10, 8, 3, 2, 19, 9, 11};

        bubbleSort(data, false);
        heapSort(data, data.length - 1);
        bubbleSort(data, true);
    }
}
