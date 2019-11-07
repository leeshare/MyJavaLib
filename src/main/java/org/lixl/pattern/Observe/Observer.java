package org.lixl.pattern.Observe;

/**
 * Created by Administrator on 11/7/2019.
 * 观察者
 */
public interface Observer {

    //观察者需要 及时更新
    void update(String newState);
}
