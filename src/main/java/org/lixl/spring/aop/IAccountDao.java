package org.lixl.spring.aop;

/**
 * Created by Administrator on 7/6/2018.
 */
public interface IAccountDao {
    void insertAccount(String aname, double money);

    void updateAccount(String aname, double money, boolean isBuy);
}
