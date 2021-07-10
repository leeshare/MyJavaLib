package org.lixl.hadoop.lixlsource.common.ipc;


//import org.lixl.hadoop.ipc.protobuf.RpcHeaderProtos.RpcRequestHeaderProto.RpcErrorCodeProto;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 1/9/2020.
 */
public class RemoteException extends IOException {
    private static final int UNSPECIFIED_ERROR = -1;
    //选中类名，ALT + Enter
    private static final long serialVersionUID = 9203831460473656086L;
    //private final int errorCode;
    //private final String className;
    private int errorCode;
    private String className;

    public RemoteException(String className, String msg) {
        //this(className, msg, null);
    }

    /*public RemoteException(String className, String msg, RpcErrorCodeProto erCode) {
        super(msg);
        this.className = className;
        if(erCode != null)
            errorCode = erCode.getNumber();
        else
            errorCode = UNSPECIFIED_ERROR;
    }*/
    public String getClassName() {
        return className;
    }
    /*public RpcErrorCodeProto getErrorCode(){
        return RpcErrorCodeProto.valueOf(errorCode);
    }*/

    public IOException unwrapRemoteException(Class<?>... lookupTypes) {
        if (lookupTypes == null)
            return this;
        for (Class<?> lookupClass : lookupTypes) {
            if (!lookupClass.getName().equals(getClassName()))
                continue;
            try {
                return instantiateException(lookupClass.asSubclass(IOException.class));
            } catch (Exception e) {
                // cannot instantiate lookupClass, just return this
                return this;
            }
        }

        return this;
    }

    public IOException unwrapRemoteException() {
        try {
            Class<?> realClass = Class.forName(getClassName());
            return instantiateException(realClass.asSubclass(IOException.class));
        } catch (Exception e) {
            // cannot instantiate the original exception. just return this
        }
        return this;
    }

    private IOException instantiateException(Class<? extends IOException> cls) throws Exception {
        Constructor<? extends IOException> cn = cls.getConstructor(String.class);
        cn.setAccessible(true);
        IOException ex = cn.newInstance(this.getMessage());
        ex.initCause(this);
        return ex;
    }

    public static RemoteException valueOf(Attributes attrs) {
        return new RemoteException(attrs.getValue("class"), attrs.getValue("message"));
    }

    public String toString() {
        return getClass().getName() + "(" + className + "): " + getMessage();
    }
}
