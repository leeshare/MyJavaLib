package org.lixl.hadoop.lixlsource.fs;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

@InterfaceAudience.Private
@InterfaceStability.Unstable
public class CommonConfigurationKeys extends CommonConfigurationKeysPublic {

    public static final String FS_PERMISSIONS_UMASK_KEY = "fs.permissions.unmask-mode";

    public static final String NET_TOPOLOGY_CONFIGURED_NODE_MAPPING_KEY = "net.topology.configured.node.mapping";

    public static final String NFS_EXPORTS_ALLOWED_HOSTS_KEY = "nfs.exports.allowed.hosts";

}
