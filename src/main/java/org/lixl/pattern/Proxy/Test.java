package org.lixl.pattern.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 11/11/2019.
 */
public class Test {

    public static void main(String[] args) {
        staticProxy();

        dynamicProxy();
    }

    private static void staticProxy(){
        //目标对象
        ISinger target = new Singer();
        //代理对象
        ISinger proxy = new SingerProxy(target);
        //执行的是代理的方法
        proxy.sing();
    }

    private static void dynamicProxy(){
        SingerProxyDynamic proxy = new SingerProxyDynamic();
        proxy.singSong();

    }
}
