import java.util.Arrays;

/**
 * Created by lxl on 18/7/15.
 */
public class TestHeapSort {
    private static final int top = 3;
    private static void swap(int[] nums, int i, int j){
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
    private static void sortTop(int[] nums){
        int first = nums[0];
        for(int i = 1; i < top; i++){
            if(first > nums[i]){
                swap(nums, 0, i);
                first = nums[0];
            }
        }
    }
    private static void sort(int[] nums, int index){
        int first = nums[0];
        for(int i = index; i >= top; i--){
            if(nums[i] > first){
                swap(nums, 0, i);
                sortTop(nums);
                first = nums[0];
            }
        }
    }

    public static void main(String[] args){
        int[] nums = new int[]{5, 9, 4, 2, 6, 3, 8, 1, 0, 7};
        sort(nums, nums.length - 1);
        System.out.println(Arrays.toString(nums));
    }
}
