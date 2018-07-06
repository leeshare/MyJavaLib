package org.lixl.spring.aop;

/**
 * Created by Administrator on 7/6/2018.
 */
public interface IStockProcessService {
    void openAccount(String aname, double money);
    void openStock(String sname, int amount);
    void buyStock(String aname, double money, String sname, int amount) throws StockException;
}
