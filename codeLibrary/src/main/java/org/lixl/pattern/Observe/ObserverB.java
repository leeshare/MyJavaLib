package org.lixl.pattern.Observe;


/**
 * Created by Administrator on 11/7/2019.
 * 一个观察者
 */
public class ObserverB implements Observer {

    private String observerState;
    @Override
    public void update(String newState){
        observerState = newState;
        //log.info("接收到消息 {}, 我是模块B", newState);

    }
}
