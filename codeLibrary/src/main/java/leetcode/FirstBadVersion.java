package leetcode;

import java.util.*;

/**
 * 假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本。
 *      给定 n = 5，并且 version = 4 是第一个错误的版本。  则要输出 4
 *
 * 执行用时：21 ms, 在所有 Java 提交中击败了18.81%的用户
 * 这个题我使用的是二分法
 * 有些细节考虑不周，比如开始用 int[] 来存状态，会报超出内存限制。
 * beginIndex + endIndex 会超出int最大值，所以用long类型
 */
public class FirstBadVersion {

    //测试 5,4    2,2     1,1         2126753390, 1702766719
    static int n = 2126753390;
    static int firstBadVersionNo = 1702766719;
    public static void main(String[] args) {
        System.out.println(firstBadVersion(n));
    }

    public static boolean isBadVersion(int n){
        if(n >= firstBadVersionNo){
            return true;
        }
        return false;
    }

    public static int firstBadVersion(int n) {
        //int[] x = new int[n];       //1 正确； 2 失败

        Map<Integer, Integer> x = new HashMap<>();
        long bIndex = 0, eIndex = n;
        if(n >= 2){
            boolean isContinue = true;
            while(isContinue) {
                int middleIndex = (int)((eIndex + bIndex)/2);
                if(middleIndex == eIndex || middleIndex == bIndex){
                    //x[middleIndex] = isBadVersion(middleIndex+1) ? 2 : 1;
                    x.put(middleIndex, isBadVersion(middleIndex+1) ? 2 : 1);
                    isContinue = false;
                }
                if(isBadVersion(middleIndex+1)){
                    //x[middleIndex] = 2;
                    x.put(middleIndex, 2);
                    //此版错误，继续向前
                    bIndex = bIndex;
                    eIndex = middleIndex;
                } else {
                    //x[middleIndex] = 1;
                    x.put(middleIndex, 1);
                    //此版正确，继续向后
                    bIndex = middleIndex;
                    eIndex = eIndex;
                }
            }
        } else {
            //x[n-1] = isBadVersion(n) ? 2 : 1;
            x.put(n-1, isBadVersion(n) ? 2 : 1);
        }


        Set<Map.Entry<Integer, Integer>> set = x.entrySet();
        Iterator<Map.Entry<Integer, Integer>> iterator = set.iterator();
        int minIndex = n;
        while (iterator.hasNext()){
            Map.Entry<Integer, Integer> entry = iterator.next();
            if(entry.getValue() == 2){
                int index = entry.getKey() + 1;
                minIndex = Math.min(minIndex, index);
            }
        }
        /*for(int i = 0; i < x.length; i++){
            if(x[i] == 2){
                return i+1;
            }
        }*/
        return minIndex;
    }
}
