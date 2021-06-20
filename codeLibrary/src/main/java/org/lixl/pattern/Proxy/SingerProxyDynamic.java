package org.lixl.pattern.Proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 11/11/2019.
 * 2 动态代理
 *  缺点：目标对象必须实现一个或多个接口
 */
public class SingerProxyDynamic {
    //private Logger log = LoggerFactory.getLogger(SingerProxyDynamic.class);

    public void singSong(){
        Singer target = new Singer();
        ISinger proxy = (ISinger) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //log.info("动态代理--------");
                //log.info("向观众问好");
                Object returnValue = method.invoke(target, args);
                //log.info("谢谢大家");
                return returnValue;

            }
        });
        proxy.sing();
    }
}
