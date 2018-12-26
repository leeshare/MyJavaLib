package org.lixl.java.lang.iface;

import java.io.IOException;

/**
 * compare with java.lang.Appendable
 * Created by lxl on 18/12/26.
 */
public interface LAppendable {

    LAppendable append(LCharSequence csq) throws IOException;

    LAppendable append(LCharSequence csq, int start, int len) throws IOException;

    LAppendable append(char c) throws IOException;
}
