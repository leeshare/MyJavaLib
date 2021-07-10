package leetcode;

/**
 * 每次爬1或2级台阶，对于n级台阶有多少中爬法。
 * 我们发现规律：第n级台阶爬法 = 第n-1级爬法 + 第n-2级爬法
 * <p>
 * 如果每次1或2或3级，则 第n级台阶爬法 = 第n-1级 + 第n-2级 + 第n-3级爬法
 * <p>
 * 爬楼梯问题 = 斐波那契数列问题
 */
public class ClimbStairs {

    static int[] memory;

    public static void main(String[] args) {

        int n = 45; //再大就溢出了
        //System.out.println(climb(n));
        System.out.println(climbWithMemory(n));

        System.out.println(climbStair2(n));
    }

    /**
     * 动态规划
     * <p>
     * 规律  1 ———— 1
     * 2 ———— 2
     * 3 ———— 3
     * 4 ———— 5
     * 5 ———— 8
     * 这个方法的问题在于：有很多重复计算，比如计算第4，第5级，都要重复计算第3级
     *
     * @param n
     * @return
     */
    public static int climb(int n) {
        if (n <= 2) {
            return n;
        } else {
            return climb(n - 1) + climb(n - 2);
        }
    }

    /**
     * 记忆化递归
     *
     * @param n
     * @return
     */
    public static int climbWithMemory(int n) {
        if (memory == null) {
            memory = new int[n + 1];
        }
        if (memory[n] > 0) {
            return memory[n];
        }
        if (n <= 2) {
            memory[n] = n;
        } else {
            memory[n] = climbWithMemory(n - 1) + climbWithMemory(n - 2);
        }
        return memory[n];

    }

    public static int climbStair2(int n) {
        if (n <= 2) {
            return n;
        }
        int first = 1;
        int second = 2;
        for (int i = 3; i <= n; i++) {
            int temp = first + second;
            first = second;
            second = temp;
        }
        return second;
    }
}
