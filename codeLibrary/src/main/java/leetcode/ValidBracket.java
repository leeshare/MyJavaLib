package leetcode;

import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 */
public class ValidBracket {
    private static HashMap<String, String> branckets = new HashMap<>();
    private static ArrayList<String> heap = new ArrayList<>();


    public static void main(String[] args) {
        branckets.put("{", "}");
        branckets.put("[", "]");
        branckets.put("(", ")");

        String s = "{[]{}({}[({})])}";
        System.out.println(valid(s));
        s = "{[]{}({}][({})])}";
        System.out.println(valid(s));

    }

    private static boolean valid(String s) {
        for(int i = 0; i < s.length(); i++){
            heap.add(s.substring(i, i + 1));
            int size = heap.size();
            if(size >= 2){
                String right = heap.get(size - 1);
                String left = heap.get(size - 2);
                if(branckets.containsKey(left) && branckets.get(left).equalsIgnoreCase(right)){
                    heap.remove(size - 1);
                    heap.remove(size - 2);
                }
            }
        }
        if(heap.size() == 0){
            return true;
        }
        return false;
    }

    //想用递归写，写不下去
    private static String checkAndRemove(String expect, String s) {
        if(s.startsWith(expect)){
            String ss = s.substring(1, s.length());
            return ss;
        }else if(s.length() > 0){
            String head = s.substring(0, 1);
            String tail = branckets.get(head);
            String ss = checkAndRemove(tail, s.substring(1, s.length()));

        }
        return "";
    }
}
