package resume;

public class QuickSort {
    public static void swap(int[] nums, int x, int y){
        int t = nums[x];
        nums[x] = nums[y];
        nums[y] = t;
    }

    public static void sort(int[] nums, int left, int right){
        if(left >= right){
            return;
        }
        int s = nums[left];
        int i = left;
        int j = right + 1;

        while(true){
            while(i < nums.length - 1 && nums[++i] < s);
            while(j > 0 && nums[--j] > s);
            if(i >= j){
                break;
            }
            swap(nums, i, j);
        }

        nums[left] = nums[j];
        nums[j] = s;

        sort(nums, left, j - 1);
        sort(nums, j + 1, right);

    }

    public static void main(String[] args){
        int[] nums = {9, 4, 6, 3, 5, 8, 0, 2, 7, 1};
        sort(nums, 0, nums.length - 1);
    }
}
