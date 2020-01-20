package org.lixl.hadoop.lixlsource.io;

import java.util.Comparator;

/**
 * Created by lxl on 20/1/20.
 */
public interface RawComparator<T> extends Comparator<T> {

    /**
     * 在二进制下比较两个对象
     * @param b1    第一个字节数组
     * @param s1    b1中的索引位置.
     * @param l1
     * @param b2
     * @param s2
     * @param l2
     * @return
     */
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2);
}
