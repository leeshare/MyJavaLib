package org.lixl.aop;

import jdk.nashorn.internal.runtime.Context;

/**
 * Created by lxl on 18/9/28.
 */
public class HelloWorldC implements HelloWorld {

    public void printHelloWorld(){
        System.out.println("Enter HelloWorldC.printHelloWorld()");
    }

    public void doPrint(){
        System.out.println("Enter HelloWorldC.doPrint()");
    }
}
