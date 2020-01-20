package org.lixl.hadoop.lixlsource.io;

/**
 * Created by lxl on 20/1/20.
 */
public class BinaryComparable implements Comparable<BinaryComparable> {

    public abstract int getLength();

    public abstract byte[] getBytes();

    public int compareTo(BinaryComparable other) {
        if(this == other)
            return 0;
        return WritableCom
    }
}
