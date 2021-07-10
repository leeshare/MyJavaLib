package leetcode;

import java.util.HashSet;

public class IsValidSudoku {
    public static void main(String[] args) {
        String[][] board = new String[9][9];
        //把第一个5 换成 8 即无效
        board[0] = new String[]{"5", "3", ".", ".", "7", ".", ".", ".", "."};
        board[1] = new String[]{"6", ".", ".", "1", "9", "5", ".", ".", "."};
        board[2] = new String[]{".", "9", "8", ".", ".", ".", ".", "6", "."};
        board[3] = new String[]{"8", ".", ".", ".", "6", ".", ".", ".", "3"};
        board[4] = new String[]{"4", ".", ".", "8", ".", "3", ".", ".", "1"};
        board[5] = new String[]{"7", ".", ".", ".", "2", ".", ".", ".", "6"};
        board[6] = new String[]{".", "6", ".", ".", ".", ".", "2", "8", "."};
        board[7] = new String[]{".", ".", ".", "4", "1", "9", ".", ".", "5"};
        board[8] = new String[]{".", ".", ".", ".", "8", ".", ".", "7", "9"};


        System.out.println(valid(board));


        String[][] aa = new String[][]{
                {".", ".", ".", ".", ".", "3", "2", ".", "4"},
                {".", ".", ".", ".", "2", ".", ".", ".", "."},
                {".", ".", ".", ".", ".", ".", ".", ".", "."},
                {".", "6", ".", ".", ".", ".", "7", ".", "."},
                {".", ".", ".", ".", ".", ".", ".", ".", "."},
                {".", ".", ".", ".", "9", ".", ".", ".", "."},
                {"3", ".", ".", "1", ".", ".", ".", "8", "."},
                {".", ".", ".", ".", ".", ".", ".", ".", "."},
                {".", ".", ".", ".", ".", ".", ".", ".", "."}};
        System.out.println(valid(aa));
    }

    private static boolean valid(String[][] board) {
        HashSet<String> map1;
        HashSet<String> map2;
        HashSet<String> map3;
        // {0,0} 和 {0,1} {0,8}
        // {0,0} 和 {1,0} {8,0}
        // {0,0} 和 {0,1} {0,2} {1,0}{1,1}{1,2} {2,0}{2,1}{2,2}
        for (int i = 0; i < 9; i++) {
            map1 = new HashSet<>();
            map2 = new HashSet<>();
            map3 = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != "." && !map1.add(board[i][j])) {
                    return false;
                }
                if (board[j][i] != "." && !map2.add(board[j][i])) {
                    return false;
                }
                int k = (i / 3) * 3 + j / 3;
                int l = (i % 3) * 3 + j % 3;
                if (board[k][l] != "." && !map3.add(board[k][l])) {
                    return false;
                }
            }
        }
        return true;
    }
}
