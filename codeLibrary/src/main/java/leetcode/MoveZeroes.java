package leetcode;

import java.util.Arrays;

/**
 * 移动零：将零移到数组结尾，其他非零顺序保持不变
 * <p>
 * 双指针：一个指针指向第1个零；另一个指针指向第一个非零
 * 细节：只有符合且非零在左，才交互位置
 * 否则虽符合但位置不对，则零指针不动，非零向后走
 */
public class MoveZeroes {
    public static void main(String[] args) {

        int[] nums = new int[]{0, 1, 0, 3, 12};
        moveZeroes(nums);
        System.out.println(Arrays.toString(nums));

        nums = new int[]{1, 0};
        moveZeroes(nums);
        System.out.println(Arrays.toString(nums));

        nums = new int[]{1, 0, 1};
        moveZeroes(nums);
        System.out.println(Arrays.toString(nums));
    }

    private static void moveZeroes(int[] nums) {
        int i0 = 0, i1 = 0;
        while (i0 < nums.length && i1 < nums.length) {
            if (nums[i0] != 0) {
                i0++;
            } else if (nums[i1] == 0) {
                i1++;
            } else {
                if (i1 > i0) {
                    int temp = nums[i0];
                    nums[i0] = nums[i1];
                    nums[i1] = temp;
                    i0++;
                }
                i1++;
            }


        }

    }


}
