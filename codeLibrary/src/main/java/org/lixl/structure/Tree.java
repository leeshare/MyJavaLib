package org.lixl.structure;

/**
 * 二叉树的类
 * Created by lxl on 18/7/5.
 */
public class Tree {
    //根节点
    private Node root;

    public Tree() {

    }

    public Tree(Node root) {
        this.root = root;
    }

    /**
     * 插入节点
     *
     * @param key
     * @param value
     */
    public void insert(int key, Object value) {
        Node node = new Node(key, value);
        if (this.root == null) {
            this.root = node;
        } else {
            Node currentNode = this.root;
            while (true) {
                if (key > currentNode.key) {
                    if (currentNode.rightChildNode == null) {
                        currentNode.rightChildNode = node;
                        return;
                    } else {
                        currentNode = currentNode.rightChildNode;
                    }
                } else {
                    if (currentNode.leftChildNode == null) {
                        currentNode.leftChildNode = node;
                        return;
                    } else {
                        currentNode = currentNode.leftChildNode;
                    }
                }
            }
        }
    }

    /**
     * 查找节点
     *
     * @param key
     * @return
     */
    public Node find(int key) {
        if (this.root != null) {
            Node currentNode = this.root;
            while (currentNode.key != key) {
                if (key > currentNode.key) {
                    currentNode = currentNode.rightChildNode;
                } else {
                    currentNode = currentNode.leftChildNode;
                }
                if (currentNode == null) {
                    return null;
                }
            }

        }
        return null;
    }

    /**
     * 采用中序遍历法
     *
     * @param node
     */
    public void show(Node node) {
        if (node != null) {
            this.show(node.leftChildNode);
            System.out.println(node.key + ":" + node.value);
            this.show(node.rightChildNode);
        }
    }

    public void show() {
        this.show(root);
    }
}
