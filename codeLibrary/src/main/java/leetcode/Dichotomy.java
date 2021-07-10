package leetcode;

import java.util.Arrays;

/**
 * 二分法
 * 快速在一个数组中，找出 是否存在 给定数
 */
public class Dichotomy {
    static int[] A = new int[]{1, 2, 3, 4, 5, 6, 7};

    public static void main(String[] args) {

        int x = 2;
        int result = find(A, x);
        System.out.println(result);
        result = findIndex(A, x);
        System.out.println(result);
    }

    private static int find(int[] a, int x) {
        while (true) {
            if (a.length >= 2) {
                int middle = a.length / 2;
                if (a[middle] == x) {
                    return a[middle];
                } else if (a[middle] < x) {
                    int[] big = Arrays.copyOfRange(a, middle, a.length);
                    a = big;
                } else {
                    int[] small = Arrays.copyOfRange(a, 0, middle);
                    a = small;
                }
            } else {
                return a.length == 0 ? -1 : a[0] == x ? a[0] : -1;
            }
        }
    }

    private static int findIndex(int[] a, int x) {
        int b = 0, e = a.length;
        if (a.length >= 2) {
            while (true) {
                int middle = (e - b) / 2;
                if (middle < b) {
                    return -1;
                }
                if (a[middle] == x) {
                    return middle;
                } else if (a[middle] < x) {
                    //int[] big = Arrays.copyOfRange(a, middle, a.length);
                    //a = big;
                    b = middle;
                    e = e;
                } else {
                    //int[] small = Arrays.copyOfRange(a, 0, middle);
                    //a = small;
                    b = b;
                    e = middle - 1;
                }
            }
        } else {
            return a[b] == x ? b : -1;
        }
    }
}