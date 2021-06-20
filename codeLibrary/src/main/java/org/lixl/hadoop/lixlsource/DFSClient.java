package org.lixl.hadoop.lixlsource;

import org.apache.htrace.core.TraceScope;
import org.lixl.hadoop.lixlsource.common.fs.Options;
import org.lixl.hadoop.lixlsource.common.ipc.RemoteException;

import java.io.IOException;
import java.security.AccessControlException;

/**
 * Created by Administrator on 1/9/2020.
 */
public class DFSClient implements Cloneable//, RemotePee
{

    //private final Tracer
    volatile boolean clientRunning = true;

    void checkOpen() throws IOException {
        if(!clientRunning) {
            throw new IOException("Filesystem closed");
        }
    }

    public void rename(String src, String dst, Options.Rename... options) throws IOException {
        checkOpen();
/*        try(TraceScope ignored = newStrDstTraceScope("rename", src, dst)) {
            //return namenode.
        } catch (RemoteException re) {
            throw re.unwrapRemoteException(AccessControlException.class
                    //NSQuota
            );
        }*/
    }



    TraceScope newStrDstTraceScope(String description, String src, String dst) {
        //TraceScope scope = tracer

        return null;
    }


}
