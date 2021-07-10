package hadoop;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 自己定制的 Writable 类
 * 表示一对字符串的实现
 */
public class TextPair implements WritableComparable<TextPair> {
    private Text first;
    private Text second;

    public TextPair() {
        set(new Text(), new Text());
    }
    public TextPair(String first, String second) {
        set(new Text(first), new Text(second));
    }
    public TextPair(Text first, Text second) {
        set(first, second);
    }

    public void set(Text first, Text second) {
        this.first = first;
        this.second = second;
    }

    public Text getFirst() {
        return first;
    }
    public Text getSecond() {
        return second;
    }


    @Override
    public int compareTo(TextPair o) {
        int cmp = first.compareTo(o.first);
        if(cmp != 0) {
            return cmp;
        }
        return second.compareTo(o.second);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        first.write(dataOutput);
        second.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        first.readFields(dataInput);
        second.readFields(dataInput);
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 163 + second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TextPair) {
            TextPair tp = (TextPair) obj;
            return first.equals(tp.first) && second.equals(tp.second);
        }
        return false;
    }
    @Override
    public String toString() {
        return first + "\t" + second;
    }
}
