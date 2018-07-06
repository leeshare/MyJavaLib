package org.lixl.spring.aop;

/**
 * Created by Administrator on 7/6/2018.
 */
public interface IStockDao {
    void insertStock(String sname, int amount);

    void updateStock(String sname, int account, boolean isBuy);
}
