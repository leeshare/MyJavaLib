package org.lixl.pattern.Observe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 11/7/2019.
 * 被观察者 或 Observable
 *      其维护一个 观察者列表  mObservers, 这样就和观察者之间变得松耦合了
 *
 */
public class Subject {
    private static Logger log = LoggerFactory.getLogger(Subject.class);

    //保存注册的观察者对象
    private List<Observer> mObservers = new ArrayList<>();

    //注册一个观察者对象
    public void attach(Observer observer){
        mObservers.add(observer);
        log.info("Lxl attach an observer");
    }

    //销毁观察者对象
    public void detach(Observer observer){
        mObservers.remove(observer);
        log.info("Lxl detach an observer");
    }

    //通知所有注册的观察者对象
    public void notifyEveryone(String newState){
        for(Observer observer : mObservers){
            observer.update(newState);
        }
    }


}
