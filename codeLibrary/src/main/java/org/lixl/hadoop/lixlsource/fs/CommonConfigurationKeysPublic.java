package org.lixl.hadoop.lixlsource.fs;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

@InterfaceAudience.Public
public class CommonConfigurationKeysPublic {
    public static final String NET_TOPOLOGY_SCRIPT_NUMBER_ARGS_KEY = "net.topology.script.number.args";
    public static final int NET_TOPOLOGY_SCRIPT_NUMBER_ARGS_DEFAULT = 100;

    public static final String FS_DEFAULT_NAME_KEY = "fs.defaultFS";
    public static final String FS_DEFAULT_NAME_DEFAULT = "file:///";
    public static final String FS_DF_INTERVAL_KEY = "fs.df.interval";
    public static final long FS_DF_INTERVAL_DEFAULT = 60000;
    public static final String FS_DU_INTERVAL_KEY = "fs.du.interval";
    public static final long FS_DU_INTERVAL_DEFAULT = 600000;

    public static final String FS_CLIENT_RESOLVE_REMOTE_SYMLINKS_KEY = "fs.client.resolve.remote.symlinks";
    public static final boolean FS_CLIENT_RESOLVE_REMOTE_SYMLINKS_DEFAULT = true;

    //默认不指定以下键
    public static final String NET_TOPOLOGY_SCRIPT_FILE_NAME_KEY = "net.topology.script.file.name";

    public static final String NET_TOPOLOGY_NODE_SWITCH_MAPPING_IMPL_KEY = "net.topology.node.switch.mapping.impl";

    public static final String HADOOP_SYSTEM_TAGS = "hadoop.system.tags";
    public static final String HADOOP_CUSTOM_TAGS = "hadoop.custom.tags";


    public static final String HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS = "hadoop.security.sensitive-config-keys";
    public static final String HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS_DEFAULT = String.join(",",
            "secret$",
            "password$",
            "ssl.keystore.pass$",
            "fs.s3.*[Ss]ecret.?[Kk]ey",
            "fs.s3a.*.server-side-encryption.key",
            "fs.azure\\.account.key.*",
            "credential$",
            "oauth.*token$",
            HADOOP_SECURITY_SENSITIVE_CONFIG_KEYS
    );

    /**
     * @see <a href="{@docRoot}/../hadoop-project-dist/hadoop-common/core-default.xml">
     * core-default.xml</a>
     */
    public static final String IO_FILE_BUFFER_SIZE_KEY = "io.file.buffer.size";
    /**
     * Default value for IO_FILE_BUFFER_SIZE_KEY
     */
    public static final int IO_FILE_BUFFER_SIZE_DEFAULT = 4096;

}
