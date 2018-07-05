package org.lixl.maths.arithmetic;

/**
 * Created by Administrator on 6/29/2018.
 */
public class Arithmetics {

    /**
     * 快速排序
     * 平均时间复杂度 O(nlogn)，最坏 O(n^2)
     * @param arr
     * @param low
     * @param high
     */
    private static void quick_sort(int[] arr, int low, int high) {
        // 解决和合并
        if (low <= high) {
            int mid = partition(arr, low, high);
            // 递归
            quick_sort(arr, low, mid - 1);
            quick_sort(arr, mid + 1, high);
        }

    }

    private static int partition(int[] arr, int low, int high) {
        // 分解
        int pivot = arr[high];
        int i = low - 1;
        int temp;
        for (int j = low; j < high; j++) {

            if (arr[j] < pivot) {
                i++;
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // 交换中间元素和privot
        temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;

    }

}
