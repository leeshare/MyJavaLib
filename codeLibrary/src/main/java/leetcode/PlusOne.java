package leetcode;

public class PlusOne {
    public static void main(String[] args) {

    }

    private static int[] plus_one(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i] += 1;
                return digits;
            } else {
                digits[i] = 0;
            }
        }
        int[] result = new int[digits.length + 1];
        result[0] = 1;
        for (int i = 0; i <= digits.length; i++) {
            result[i] = digits[i];
        }
        return result;
    }
}
