package leetcode;

import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 中序遍历
 * 比如二叉树是 [1,null,2,3] -> [1,3,2]
 * [10,5,null,null,15,6,2] -> [5,10,6,15,2]
 */
public class InorderTraversal {
    public static void main(String[] args) {

        TreeNode left = new TreeNode(3);
        TreeNode left2 = new TreeNode(2, left, null);
        TreeNode root = new TreeNode(1, null, left2);

        List<Integer> list = toInorderTraversal(root);
        System.out.println(list.toString());
    }

    public static List<Integer> middleTree = new ArrayList<>();

    private static List<Integer> toInorderTraversal(TreeNode root) {
        if (root == null) {
            return middleTree;
        }
        toInorderTraversal(root.left);
        middleTree.add(root.val);
        toInorderTraversal(root.right);

        return middleTree;
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
