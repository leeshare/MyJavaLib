package org.lixl.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 创建自定义的 InvocationHandler ，用于对接口提供的方法进行增强
 * Created by Administrator on 7/4/2018.
 */
public class MyInvocationHandler implements InvocationHandler {

    //目标对象
    private Object target;

    public MyInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    /**
     * 执行目标对象的方法
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("------------before-------------");

        //执行目标对象的方法
        Object result = method.invoke(target, args);

        //执行后
        System.out.println("------------after--------------");

        return result;
    }

    /**
     * 获取目标对象的代理对象
     *
     * @return
     */
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }
}
