package leetcode;

import java.io.StringReader;
import java.util.*;

/**
 * 请取出只出现一次的字母（其他最多出现了2次）
 */
public class ExactlyOneLetter {

    public static void main(String[] args) {
        String str = "abcdfcsdfa";

        System.out.println(getTheLetter(str));
    }

    private static String getTheLetter(String str) {
        int[] sign = new int[str.length()];
        StringBuilder result = new StringBuilder();
        List<String> arr = Arrays.asList(str.split(""));
        for (int i = 0; i < arr.size(); i++) {
            if (sign[i] == 1) {
                continue;
            }
            for (int j = arr.size() - 1; j > i; j--) {
                if (sign[j] == 1) {
                    continue;
                }
                if (arr.get(i).equals(arr.get(j))) {
                    sign[i] = 1;
                    sign[j] = 1;
                    break;
                }
            }
        }
        for (int i = 0; i < sign.length; i++) {
            if (sign[i] == 0) {
                result.append(arr.get(i));
            }
        }
        return result.toString();

    }
}
