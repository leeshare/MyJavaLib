import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 18/7/5.
 */
public class TestQuick2 {
    static AtomicInteger quick_count = new AtomicInteger();
    static void swap(int[] numbers, int i, int j){
        int t = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = t;
        System.out.println("swap " + Arrays.toString(numbers));
    }
    static void quick_sort(int[] numbers, int left, int right){
        if(left >= right){
            return;
        }
        int s = numbers[left];

        int i = left;
        int j = right + 1;
        while(true){
            while(i < numbers.length - 1 && numbers[++i] < s){
                quick_count.incrementAndGet();
            };
            while(j > 0 && numbers[--j] > s){
                quick_count.incrementAndGet();
            };
            if(i >= j){
                break;
            }
            swap(numbers, i, j);
        }

        numbers[left] = numbers[j];
        numbers[j] = s;
        System.out.println("quick_sort " + Arrays.toString(numbers));

        quick_sort(numbers, left, j - 1);
        quick_sort(numbers, j + 1, right);

    }
    static void quick_sort(int[] numbers){
        quick_sort(numbers, 0, numbers.length - 1);

        int count = quick_count.get();
        System.out.println("quick_count=" + count);
    }

    static void bubble_sort(int[] numbers){
        int count = 0;
        for(int i = 0; i < numbers.length; i++){
            for(int j = i; j < numbers.length; j++){
                count ++;
                if(j < i){
                    swap(numbers, i , j);
                }
            }
        }
        System.out.println("bubble_count=" + count);
        System.out.println("bubble_sort " + Arrays.toString(numbers));
    }

    public static void main(String[] args){
        //int[] numbers = new int[]{5, 2, 9, 3, 8, 4, 7, 6, 1, 0};
        int[] numbers = new int[]{1, 2, 9, 3, 8, 5, 7, 6, 4, 0};

        quick_sort(numbers);

        System.out.println(" ------ ");

        bubble_sort(numbers);
    }

}
