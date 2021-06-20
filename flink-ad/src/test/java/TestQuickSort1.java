import java.util.Arrays;

/**
 * Created by lxl on 18/7/1.
 */
public class TestQuickSort1 {

    private static void swap(int[] number, int i, int j){
        int t;
        t = number[i];
        number[i] = number[j];
        number[j] = t;
        System.out.println(Arrays.toString(number));
    }

    public static void sort(int[] number, int left, int right){
        if(left >= right){
            return;
        }
        int s = number[left];

        int i = left;
        int j = right + 1;
        while(true){
            while(i + 1 < number.length && number[++i] < s);
            while(j - 1 > -1 && number[--j] > s);
            if(i >= j){
                break;
            }
            swap(number, i, j);
        }
        number[left] = number[j];
        number[j] = s;
        sort(number, left, j - 1);
        sort(number, j + 1, right);
    }

    public static void sort(int[] number){
        sort(number, 0, number.length - 1);

        System.out.println(Arrays.toString(number));
    }

    public static void main(String[] args){
        int[] number = new int[]{41, 24, 76, 11, 45, 64, 21, 69, 19, 36};
        System.out.println("排序开始:" + Arrays.toString(number));
        sort(number);
    }
}
