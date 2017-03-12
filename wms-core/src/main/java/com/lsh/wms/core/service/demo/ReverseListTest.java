public class ReverseListTest {

    static class ListNode {
        int key;
        ListNode next;


    }

    public static ListNode reverse(ListNode pHead) {
        ListNode reverseHead = pHead;
        ListNode node = pHead;
        ListNode prevNode = null;

        while (node != null) {
            ListNode next = node.next;
            if (next == null) {
                reverseHead = node;

            }
            node.next = prevNode;
            prevNode = node;
            node = next;
        }

        return reverseHead;

    }

    public static void main(String[] args) {

        // 建立一个测试链表
        ListNode head = new ListNode();
        head.key = 0;
        ListNode p = head;
        for (int i = 1; i < 10; i++) {
            ListNode node = new ListNode();
            node.key = i;
            node.next = null;
            p.next = node;
            p = node;
        }
        ListNode pNode = reverse(head);

        while(pNode != null){
            System.out.println(pNode.key);
            pNode = pNode.next;
        }

    }

}