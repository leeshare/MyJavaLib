package org.lixl.hadoop.lixlsource.common.ipc;

import java.io.IOException;

/**
 * Created by Administrator on 1/9/2020.
 */
public class RemoteException extends IOException {
    private static final int UNSPECIFIED_ERROR = -1;
    //选中类名，ALT + Enter
    private static final long serialVersionUID = 9203831460473656086L;
    private final int errorCode;
    private final String className;

    public RemoteException(String className, String msg) {
        this(className, msg, null);
    }
    public RemoteException(String className, String msg, )
}
