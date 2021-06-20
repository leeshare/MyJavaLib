package leetcode;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 给定一个数组，将数组中元素右移k位
 */
public class Rotate {
    public static void main(String[] args) {

        int[] nums = new int[]{1,2,3,4,5,6,7,8};
        int k = 2;
        //k = Math.min(k, nums.length - k);
        int len = nums.length;
        // [1,2,3,4,5,6]
        // [7,8]
        // ->[7,8,1,2,3,4,5,6]

        int[] arr = new int[len];
        for(int i = 0; i < len; i++){
            arr[(i + k)%len] = nums[i];
        }
        System.arraycopy(arr, 0, nums, 0, len);

        System.out.println(ArrayUtils.toString(nums));
    }
}
