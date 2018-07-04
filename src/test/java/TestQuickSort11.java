import java.util.Arrays;

/**
 * Created by lxl on 18/7/1.
 */
public class TestQuickSort11 {

    public static void swap(int[] number, int i, int j){
        int t;
        t = number[i];
        number[i] = number[j];
        number[j] = t;
        System.out.println("swap:" + Arrays.toString(number));
    }

    public static void sort(int[] number, int left, int right){
        if(left >= right){
            return;
        }
        int s = number[left];

        int i = left;
        int j = right + 1;
        while(true){
            while(i < number.length -1 && number[++i] < s);
            while(j > 0 && number[--j] > s);
            if(i >= j){
                break;
            }
            swap(number, i, j);
        }

        number[left] = number[j];
        number[j] = s;
        System.out.println("sort:" + Arrays.toString(number));

        sort(number, left, j - 1);
        sort(number, j+1, right);
    }

    public static void sort(int[] number){
        sort(number, 0, number.length - 1);

        System.out.println("final:" + Arrays.toString(number));
    }

    public static void main(String[] args){
        int[] number = new int[]{ 5, 8, 6, 3, 1, 9, 4, 2, 7, 0};
        sort(number);
    }
}
