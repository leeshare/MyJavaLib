package org.lixl.hadoop.lixlsource.io;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@InterfaceAudience.Public
@InterfaceStability.Stable
public final class WritableUtils {


    public static void writeVInt(DataOutput stream, int i) throws IOException {

        // ...
    }

    public static int readVInt(DataInput stream) throws IOException {
        long n = 0; //  readVLong(stream);
        if ((n > Integer.MAX_VALUE) || (n < Integer.MIN_VALUE)) {
            throw new IOException("值太大超出integer范围");
        }
        return (int) n;
    }

    public static void skipFully(DataInput in, int len) throws IOException {
        int total = 0;
        int cur = 0;
        while ((total < len) && ((cur = in.skipBytes(len - total)) > 0)) {
            total += cur;
        }
        if (total < len) {
            throw new IOException("无法跳过 " + len + "字节，或许由于到了输入结尾");
        }
    }

    public static int decodeVIntSize(byte value) {
        if (value >= -112)
            return 1;
        else if (value < -120)
            return -119 - value;
        return -111 - value;
    }

    public static int readVIntInRange(DataInput stream, int lower, int upper) throws IOException {
        long n = 0L; //readVLong(stream);
        if (n < lower) {
            if (lower == 0)
                throw new IOException("除非负数整型，得到" + n);
            else
                throw new IOException("除整型大于或等于" + lower + "，得到" + n);
        }
        if (n > upper)
            throw new IOException("除整型小于或等于" + upper + "，得到" + n);
        return (int) n;
    }


}
