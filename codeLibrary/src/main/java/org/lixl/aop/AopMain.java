package org.lixl.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lxl on 18/9/28.
 */
public class AopMain {

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("aop.xml");
        HelloWorld _java = (HelloWorld) ctx.getBean("helloWorldJava");
        HelloWorld _c = (HelloWorld) ctx.getBean("helloWorldC");

        _java.printHelloWorld();
        System.out.println();
        _java.doPrint();

        System.out.println();
        _c.printHelloWorld();
        System.out.println();
        _c.doPrint();

        System.out.println();
    }
}
