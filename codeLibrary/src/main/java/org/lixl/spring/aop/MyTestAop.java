package org.lixl.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 7/7/2018.
 */
public class MyTestAop {
    private IStockProcessService service;

    public void before() {
        //创建容器
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (IStockProcessService) ac.getBean("stockServiceProxy");
    }

    public void testOpen() {
        service.openAccount("张三", 10000);
        service.openStock("华为", 5);
    }

    public void testBuyStock() throws Exception {
        service.buyStock("张三", 2000, "华为", 5);
    }

    public static void main(String[] args) {

    }
}
