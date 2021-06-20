package leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 第一题：两数之和
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] a = new int[]{2,7,11,15};
        int[] result = twoSum(a, 9);
        System.out.println(Arrays.toString(result));

        a = new int[]{3,2,4};
        result = twoSum(a, 6);
        System.out.println(Arrays.toString(result));

        a = new int[]{-3, 4, 3, 90};
        result = twoSum(a, 0);
        System.out.println(Arrays.toString(result));

        a = new int[]{2, 5, 5, 11};
        result = twoSum(a, 10);
        System.out.println(Arrays.toString(result));
    }

    /*public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for(int i = 0; i < nums.length; i++){
            if(nums[i] > target){
                //continue;
            }
            for(int j = nums.length - 1; j > i; j--){
                if(nums[j] > target){
                    //continue;
                }
                if(nums[i] + nums[j] == target){
                    result = new int[]{i, j};
                    return result;
                }
            }
        }
        return result;
    }*/

    /**
     * 把所有遍历过的数的 目标数（和-此数） 及其下标保存到一个map中
     * 每循环一个数时，和map中的数比较一遍，有相同的，说明就找到了！
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {

        HashMap<Integer,Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++){
            if(map.containsKey(nums[i])){
                return new int[]{map.get(nums[i]), i};
            }
            map.put(target - nums[i], i);
        }
        return null;

    }
}
