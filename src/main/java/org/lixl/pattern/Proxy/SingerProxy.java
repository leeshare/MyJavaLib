package org.lixl.pattern.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 11/11/2019.
 */
public class SingerProxy implements ISinger {
    private Logger log = LoggerFactory.getLogger(SingerProxy.class);

    private ISinger target;
    public SingerProxy(ISinger target) {
        this.target = target;
    }

    public void sing(){
        log.info("静态代理------");
        log.info("向观众问好");
        target.sing();
        log.info("谢谢大家");
    }
}
