import org.lixl.structure.Node;
import org.lixl.structure.Tree;

/**
 * Created by lxl on 18/7/5.
 */
public class TestTree {
    public static void main(String[] args){
        Node root = new Node(50, 24);
        Tree tree = new Tree(root);
        tree.insert(20, 530);
        tree.insert(540, 520);
        tree.insert(4, 540);
        tree.insert(0, 550);
        tree.insert(8, 520);
        tree.show();

        /*
        中序遍历

                50
            20      540
         4
      0     8

      顺序: 0 -> 4 -> 8 -> 20 -> 50 -> 540


         */
    }
}
