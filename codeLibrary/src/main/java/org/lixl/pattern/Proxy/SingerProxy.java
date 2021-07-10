package org.lixl.pattern.Proxy;


/**
 * Created by Administrator on 11/11/2019.
 * 1 静态代理
 * 缺点：目标对象必须实现一个或多个接口
 */
public class SingerProxy implements ISinger {
    //private Logger log = LoggerFactory.getLogger(SingerProxy.class);

    private ISinger target;

    public SingerProxy(ISinger target) {
        this.target = target;
    }

    public void sing() {
        //log.info("静态代理------");
        //log.info("向观众问好");
        target.sing();
        //log.info("谢谢大家");
    }
}
