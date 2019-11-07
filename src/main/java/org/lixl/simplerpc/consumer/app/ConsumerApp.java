package org.lixl.simplerpc.consumer.app;

import org.lixl.simplerpc.consumer.service.CalculatorRemoteImpl;
import org.lixl.simplerpc.provider.service.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 11/7/2019.
 */
public class ConsumerApp {
    private static Logger log = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();
        int result = calculator.add(11, 2);
        log.info("result is {}", result);

    }
}
