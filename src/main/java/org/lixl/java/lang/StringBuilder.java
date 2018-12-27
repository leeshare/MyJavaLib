package org.lixl.java.lang;

import org.lixl.java.lang.iface.LAppendable;
import org.lixl.java.lang.iface.LCharSequence;

import java.util.Arrays;

/**
 * Created by lxl on 18/12/26.
 */
public class LStringBuilder implements LAppendable {

    char[] value;
    int count;

    //确保容量至少等于规定的最小值。
    void expandCapacity(int minimumCapacity){
        int newCapacity = value.length * 2 + 2;
        if(newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;
        if(newCapacity < 0){
            if(minimumCapacity < 0)
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity);
    }

    private void ensureCapacityInternal(int minimumCapacity){
        if(minimumCapacity - value.length > 0)
            expandCapacity(minimumCapacity);

    }

    private LStringBuilder appendNull(){
        int c = count;
        ensureCapacityInternal(c + 4);
        final char[] value = this.value;
        value[c++] = 'n';
        value[c++] = 'u';
        value[c++] = 'l';
        value[c++] = 'l';
        count = c;
        return this;
    }

    public LStringBuilder append(String str){
        if(str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }

    public LStringBuilder append(LCharSequence s) {
        if(s == null)
            return appendNull();
        if(s instanceof String)
            return this.append((String)s);
        if(s instanceof LStringBuilder)
            return this.append((LStringBuilder)s)

            return this.append(s, 0, s.length());

    }
}
