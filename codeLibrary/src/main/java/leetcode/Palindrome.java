package leetcode;

/**
 * 是否回文数
 *      类似于 反转一个数的方法
 */
public class Palindrome {

    public static void main(String[] args) {
        int a = 121;
        //a = 1221;
        System.out.println(isPalindrome(a));

        System.out.println(isPalindrome2(a));
    }

    public static boolean isPalindrome(int x) {
        if(x < 0){
            return false;
        }
        char[] arr = String.valueOf(x).toCharArray();
        boolean isNotFit = false;
        for(int i = 0; i < arr.length; i++) {
            int j = arr.length - 1 - i;
            if(j < i){
                break;
            }
            if(arr[i] != arr[j]){
                isNotFit = true;
                break;
            }
        }
        if(isNotFit){
            return false;
        }
        return true;
    }

    /**
     * 和 reverse 方法类似 ，一边消除，一边生成，啥时相等时就结束了
     * @param x
     * @return
     */
    public static boolean isPalindrome2(int x) {
        if (x == 0) return true;
        if (x < 0 || x % 10 == 0) return false;
        int reversed = 0;
        while (x > reversed) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        return x == reversed || x == reversed / 10;
    }
}
