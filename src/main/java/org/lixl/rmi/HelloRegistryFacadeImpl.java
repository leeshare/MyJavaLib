package org.lixl.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Administrator on 11/27/2019.
 */
public class HelloRegistryFacadeImpl extends UnicastRemoteObject implements HelloRegistryFacade {

    protected HelloRegistryFacadeImpl() throws RemoteException {
        super();
    }

    @Override
    public String helloWorld(String name) throws RemoteException {
        return "[Registry] 你好： " + name;
    }
}
