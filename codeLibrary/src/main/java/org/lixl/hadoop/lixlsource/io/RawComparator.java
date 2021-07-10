package org.lixl.hadoop.lixlsource.io;

import java.util.Comparator;

/**
 * Created by lxl on 20/1/20.
 */
public interface RawComparator<T> extends Comparator<T> {

    /**
     * 在二进制下比较两个对象
     *
     * @param b1 第一个字节数组
     * @param s1 b1中的索引位置.
     * @param l1 b1中对象的长度
     * @param b2 第二个字符数组
     * @param s2 b2中的索引位置
     * @param l2 b2中对象的长度
     * @return 比较结果的一个数字
     */
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2);
}
