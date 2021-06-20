package leetcode;

public class HasCycle {

    public static void main(String[] args) {

    }

    public static class ListNode {
        int val;
        ListNode next;
        public ListNode(int x){
            this.val = x;
            this.next = null;
        }
    }

    /**
     * 使用快慢指针：如果有环，那么一定会相遇
     * @param head
     * @return
     */
    public static boolean hasCycle(ListNode head) {
        if(head == null){
            return false;
        }
        ListNode first = head, second = head;

        while(true){
            if(first.next != null){
                first = first.next;
            }else {
                return false;
            }
            if(second.next != null && second.next.next != null){
                second = second.next.next;
            }else {
                return false;
            }

            //if(first.val == second.val)
            if(first == second)
            {
                return true;
            }
        }
    }
}
