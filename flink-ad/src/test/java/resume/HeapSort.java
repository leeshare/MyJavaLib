package resume;

public class HeapSort {
    static final int top = 5;
    public static void swap(int[] nums, int x, int y){
        int t = nums[x];
        nums[x] = nums[y];
        nums[y] = t;
    }
    public static void topSort(int[] nums){
        int first = nums[0];
        for(int i = 1; i < top; i++){
            if(first > nums[i]){
                swap(nums, 0, i);
                first = nums[0];
            }
        }
    }

    public static void sort(int[] nums, int index){
        int first = nums[0];
        for(int i = index; i >= top; i--){
            if(nums[i] > first){
                swap(nums, i, 0);
                topSort(nums);
                first = nums[0];
            }
            index--;
        }
    }

    public static void main(String[] args){
        int[] nums = {9, 4, 6, 3, 5, 8, 0, 2, 7, 1};
        topSort(nums);
        sort(nums, nums.length - 1);
    }
}
