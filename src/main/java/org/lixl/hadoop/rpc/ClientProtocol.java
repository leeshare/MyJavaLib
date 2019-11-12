package org.lixl.hadoop.rpc;

import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * Created by Administrator on 11/12/2019.
 */
public interface ClientProtocol extends VersionedProtocol {

    public static final long versionID = 1L;

    String echo(String value);
}
