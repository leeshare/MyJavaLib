package org.lixl.hadoop.lixlsource;

import org.lixl.hadoop.lixlsource.common.fs.Options;
import org.lixl.hadoop.lixlsource.common.ipc.RemoteException;

import java.io.IOException;

/**
 * Created by Administrator on 1/9/2020.
 */
public class DFSClient {

    volatile boolean clientRunning = true;

    void checkOpen() throws IOException {
        if(!clientRunning) {
            throw new IOException("Filesystem closed");
        }
    }

    public void rename(String src, String dst, Options.Rename... options) throws IOException {
        checkOpen();
        /*try() {

        } catch (RemoteException re) {
            throw re.
        }*/
    }
}
