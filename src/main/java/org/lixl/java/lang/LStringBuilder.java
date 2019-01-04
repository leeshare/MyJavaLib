package org.lixl.java.lang;

import org.lixl.java.lang.iface.LAppendable;
import org.lixl.java.lang.iface.LCharSequence;

import java.io.IOException;

/**
 * Created by lxl on 18/12/26.
 */
public class LStringBuilder implements LAppendable {


    public LStringBuilder append(LCharSequence s) {
        return null;
    }

    @Override
    public LAppendable append(LCharSequence csq, int start, int len) throws IOException {
        return null;
    }

    @Override
    public LAppendable append(char c) throws IOException {
        return null;
    }
}
