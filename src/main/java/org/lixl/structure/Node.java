package org.lixl.structure;

/**
 * Created by lxl on 18/7/5.
 */
public class Node {
    //key
    int key;
    //值
    Object value;
    //左节点
    Node leftChildNode;
    //右节点
    Node rightChildNode;

    public Node(int key, Object value){
        super();
        this.key = key;
        this.value = value;
    }
}
