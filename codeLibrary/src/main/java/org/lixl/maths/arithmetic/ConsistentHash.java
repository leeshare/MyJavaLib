package org.lixl.maths.arithmetic;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希算法（带虚拟节点）
 * Created by Administrator on 6/20/2018.
 * 一般：2台服务器：50个虚拟节点；10台：30个；20台：20个；100台：10个；500台：1个
 */
public class ConsistentHash {
    /**
     * 待加入Hash环的服务器列表
     */
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111"};
    /**
     * 真实节点列表，考虑到服务器上线、下线场景，即添加、删除的场景会比较频繁
     * 所以这里使用 LinkedList会更好
     */
    private static List<String> realNodes = new LinkedList<String>();
    /**
     * 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
     */
    private static SortedMap<Integer, String> virtualNodes = new TreeMap<Integer, String>();
    /**
     * 虚拟节点的数目，这里写死，测试用
     */
    private static final int VIRTUAL_NODES = 5;

    static {
        for(int i = 0; i < servers.length; i++){
            realNodes.add(servers[i]);
        }
        //添加虚拟节点，遍历LinkedList使用foreach循环效率会比较高
        for(String str : realNodes){
            for(int i = 0; i < VIRTUAL_NODES; i++){
                String virtualNodeName = str + "&&VN" + String.valueOf(i);
                int hash = getHash(virtualNodeName);
                System.out.println("虚拟节点【" + virtualNodeName + "】被添加，hash值为" + hash);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
        System.out.println();
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     * @param str
     * @return
     */
    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    private static String getServer(String node){
        //得到带路由的节点的Hash值
        int hash = getHash(node);
        //得到大于该Hash的所有Map
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        //第一关Key就是顺时针过去离node最近的那个节点
        Integer i = subMap.firstKey();
        //返回对应的虚拟节点名，这里截取一下
        String virtualNode = subMap.get(i);
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }

    public static void main(String[] args){
        String[] nodes = {"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};
        for(int i = 0; i < nodes.length; i++){
            System.out.println("[" + nodes[i] + "]的hash值为" + getHash(nodes[i]) + ", 被路由到节点[" + getServer(nodes[i]) + "]");
        }
    }

}
