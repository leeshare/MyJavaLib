package org.lixl.pattern.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 11/11/2019.
 */
public class Singer implements ISinger {
    private Logger log = LoggerFactory.getLogger(Singer.class);
    public void sing(){
        log.info("唱一首歌");
    }
}
