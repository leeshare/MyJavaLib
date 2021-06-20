package org.lixl.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Administrator on 11/27/2019.
 */
public class NamingClient {
    public static void main(String[] args) {
        try {
            String remoteAddr = "rmi://localhost:1100/HelloNaming";
            HelloRegistryFacade hello = (HelloRegistryFacade) Naming.lookup(remoteAddr);
            String response = hello.helloWorld("Lixl");
            System.out.println("=====> " + response + " <=====");
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
