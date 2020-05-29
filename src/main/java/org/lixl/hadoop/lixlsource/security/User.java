package org.lixl.hadoop.lixlsource.security;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.security.UserGroupInformation.AuthenticationMethod;

import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.security.Principal;

/**
 * 保存完整的用户短名称 作为一个当事人。这允许我们有一个单独类型——我们经常寻找当拾起我们的名字。
 */
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
@InterfaceStability.Evolving
class User implements Principal {
    private final String fullName;
    private final String shortName;
    private volatile AuthenticationMethod authMethod = null;
    private volatile LoginContext login = null;
    private volatile long lastLogin = 0;

    public User(String name) {
        this(name, null, null);
    }

    public User(String name, AuthenticationMethod authMethod, LoginContext login) {
        try {
            shortName = new HadoopKerberosName(name).getShortName();
        } catch (IOException e) {
            throw new IllegalArgumentException("非法主要名字 " + name + ": " + e.toString(), e);
        }
        fullName = name;

        this.authMethod = authMethod;
        this.login = login;
    }

    @Override
    public String getName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        else if(o == null || getClass() != o.getClass())
            return false;
        else
            return ((fullName.equals(((User) o).fullName)) && (authMethod == ((User) o).authMethod));
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public String toString() {
        return fullName;
    }



}

