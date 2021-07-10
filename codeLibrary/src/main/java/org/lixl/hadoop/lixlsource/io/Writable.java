package org.lixl.hadoop.lixlsource.io;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Administrator on 1/17/2020.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public interface Writable {

    void write(DataOutput out) throws IOException;

    void readFields(DataInput in) throws IOException;
}
