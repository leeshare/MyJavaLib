package resume;

public class BubbleSort {
    public static void swap(int[] nums, int x, int y){
        int t = nums[x];
        nums[x] = nums[y];
        nums[y] = t;
    }
    public static void sort(int[] nums){
        for(int i = 0; i < nums.length; i++){
            for(int j = i; j < nums.length; j++){
                if(nums[j] > nums[i]){
                    swap(nums, i, j);
                }
            }
        }
    }

    public static void main(String[] args){
        int[] nums = {9, 4, 6, 3, 5, 8, 0, 2, 7, 1};
        sort(nums);
    }
}
