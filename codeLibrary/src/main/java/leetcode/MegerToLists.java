package leetcode;

/**
 * 合并两个有序链表
 */
public class MegerToLists {

    public static class ListNode {
        int val;
        ListNode next;
        public ListNode(){}
        public ListNode(int val) {
            this.val = val;
        }
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static ListNode merge(ListNode l1, ListNode l2) {
        ListNode result = null;
        ListNode reverse = null;
        if(l1 == null && l2 == null){
            return null;
        }else if(l1 == null && l2 != null){
            return l2;
        }else if(l1 != null && l2 == null){
            return l1;
        }
        while (l1 != null || l2 != null){
            if(l1 != null && l2 != null){
                if(l1.val <= l2.val){
                    if(reverse == null){
                        reverse = new ListNode(l1.val);
                    }else {
                        reverse = new ListNode(l1.val, reverse);
                    }
                    l1 = l1.next;
                }else {
                    if(reverse == null){
                        reverse = new ListNode(l2.val);
                    }else {
                        reverse = new ListNode(l2.val, reverse);
                    }
                    l2 = l2.next;
                }
            }else if(l1 != null){
                reverse = new ListNode(l1.val, reverse);
                l1 = l1.next;
            } else if(l2 != null){
                reverse = new ListNode(l2.val, reverse);
                l2 = l2.next;
            }
        }
        while (reverse != null){
            if(result == null){
                result = new ListNode(reverse.val);
            }else {
                result = new ListNode(reverse.val, result);
            }
            reverse = reverse.next;
        }

        return result;
    }

    /**
     * 大神的做法：递归
     * @param l1
     * @param l2
     * @return
     */
    private static ListNode mergeTwoListsWithRecursive(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        ListNode res = l1.val < l2.val ? l1 : l2;
        res.next = mergeTwoListsWithRecursive(res.next, l1.val >= l2.val ? l1 : l2);
        return res;
        /*if(l1.val < l2.val){
            l1.next = mergeTwoListsWithRecursive(l1.next, l2);
            return l1;
        }else {
            l2.next = mergeTwoListsWithRecursive(l1, l2.next);
            return l2;
        }*/
    }

    /**
     * 迭代
     * @param l1
     * @param l2
     * @return
     */
    private static ListNode mergeTwoListsWithIterative(ListNode l1, ListNode l2) {
        ListNode pre = new ListNode(-1);
        while(l1 != null && l2 != null){
            if(l1.val <= l2.val){
                pre.next = l1;      //设置next
                l1 = l1.next;
            }else {
                pre.next = l2;
                l2 = l2.next;
            }
            pre = pre.next;     //将pre向前移动一位
        }
        pre.next  = l1 == null ? l2 : l1;
        return pre.next;    //去掉哑节点
    }

    public static void main(String[] args) {
        ListNode l4 = new ListNode(4);
        ListNode l2 = new ListNode(2, l4);
        ListNode l1 = new ListNode(1, l2);

        ListNode l3 = new ListNode(3, l4);
        ListNode l11 = new ListNode(1, l3);



        //ListNode l = merge(l1, l11);
        //ListNode l = merge(null, new ListNode(0));
        ListNode l = mergeTwoListsWithRecursive(l1, l11);
        while (l != null){
            System.out.println(l.val);
            l = l.next;
        }

        /*l = mergeTwoListsWithIterative(l1, l11);
        while (l != null){
            System.out.println(l.val);
            l = l.next;
        }*/
    }
}
