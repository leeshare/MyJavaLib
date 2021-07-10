import java.util.Arrays;

/**
 * 冒泡法
 * Created by lxl on 18/7/1.
 */
public class TestBubbleSort {
    static void swap(int[] numbers, int i, int j){
        int t = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = t;
        System.out.println("swap " + Arrays.toString(numbers));
    }

    public static void sort(int[] number){
        int temp = 0;
        int num = 0;
        int upnumber = 0;
        for(int i = 0; i < number.length; i++){
            for(int j = i; j < number.length - 1; j++){
                num++;
                if(number[j+1] < number[i]){
                    //temp = number[j+1];
                    //number[j+1] = number[i];
                    //number[i] = temp;
                    swap(number, i, j+1);
                    upnumber++;
                }
            }
        }
        System.out.println(Arrays.toString(number));
        System.out.println("循环次数" + num);
        System.out.println("移动次数" + upnumber);

    }

    public static void main(String[] args){
        int[] number = new int[]{41, 24, 76, 11, 45, 64, 21, 69, 19, 36};
        sort(number);
    }
}
