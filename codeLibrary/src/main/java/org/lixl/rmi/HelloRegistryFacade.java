package org.lixl.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Administrator on 11/27/2019.
 */
public interface HelloRegistryFacade extends Remote {
    String helloWorld(String name) throws RemoteException;
}
