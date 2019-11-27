package org.lixl.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Administrator on 11/27/2019.
 */
public class RegistryClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            HelloRegistryFacade hello = (HelloRegistryFacade) registry.lookup("HelloRegistry");
            String response = hello.helloWorld("Lixl");
            System.out.println("=====>" + response + "<=====");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
