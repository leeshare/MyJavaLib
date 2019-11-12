package org.lixl.pattern.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 11/12/2019.
 * 3 Cglib代理
 *  前提：引入cglib的jar文件（一般引入 spring-core-3.2.5.jar 即可）
 *          目标类不能为final
 *          目标对象的方法如果是 static/final ，那么就不会拦截，也就无法执行额外的业务方法了。
 *  优点：目标对象可以不实现接口，而使用代理。即接口的改变对其没有任何影响。
 */
public class ProxyFactory implements MethodInterceptor {
    private Logger log = LoggerFactory.getLogger(ProxyFactory.class);

    //维护目标对象
    private Object target;

    public ProxyFactory(Object object){
        this.target = object;
    }

    //给目标对象创建一个代理对象
    public Object getProxyInstance(){
        //1. 工具类
        Enhancer en = new Enhancer();
        //2. 设置父类
        en.setSuperclass(target.getClass());
        //3. 设置回调函数
        en.setCallback(this);
        //4. 创建子类
        return en.create();

    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("Cglib代理---------------");
        log.info("向观众问好");
        //执行目标对象的方法
        Object returnValue = method.invoke(target, args);
        log.info("谢谢大家");
        return returnValue;
    }

}
