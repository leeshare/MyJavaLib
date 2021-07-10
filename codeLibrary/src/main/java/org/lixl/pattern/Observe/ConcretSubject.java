package org.lixl.pattern.Observe;


/**
 * Created by Administrator on 11/7/2019.
 * 一个具体的被观察者
 */
public class ConcretSubject extends Subject {
    private String state;

    public String getState() {
        return state;
    }

    public void change(String newState) {
        state = newState;
        //log.info("Lxl: concretSubject state is {}", newState);

        //状态发生改变，通知观察者
        notifyEveryone(newState);
    }

    public static void main(String[] args) {
        ConcretSubject sub = new ConcretSubject();
        Observer a = new ObserverA();
        Observer a2 = new ObserverA();
        Observer b = new ObserverB();
        sub.attach(a);
        sub.attach(a2);
        sub.attach(b);
        sub.change("第一条");
        sub.detach(a2);
        sub.change("第二条");
    }
}
