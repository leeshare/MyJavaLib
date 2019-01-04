import java.util.Arrays;

public class Test0830 {

    public static void main(String[] args){
        int[] numbers = new int[]{ 41, 18, 6, 91, 70, 30, 2, 28, 10, 57, 80};
        bubbleSort(numbers);
        quickSort(numbers, 0, numbers.length - 1);
    }

    public static void swap(int[] numbers, int i, int j) {
        int t = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = t;
    }

    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int num = 0;
        int upnumber = 0;
        for(int i = 0; i < numbers.length; i++){
            for(int j = i; j < numbers.length; j++){
                num++;
                if(numbers[j+1] < numbers[i]){
                    swap(numbers, i, j+1);
                    upnumber++;
                }
            }
        }
        System.out.println("冒泡:" + Arrays.toString(numbers));
        System.out.println("冒泡:循环次数" + num);
        System.out.println("冒泡:移动次数" + upnumber);
    }

    public static void quickSort(int[] numbers, int left, int right){
        if(left >= right){
            return;
        }
        int s = numbers[left];

        int i = left;
        int j = right + 1;
        while(true){
            while(i < numbers.length - 1 && numbers[++i] < s);
            while(j > 0 && numbers[--j] > s);
            if(i >= j){
                break;
            }
        }
    }
}
