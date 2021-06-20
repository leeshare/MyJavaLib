package org.lixl.spring.aop;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Created by Administrator on 7/6/2018.
 */
public class StockDaoImpl extends JdbcDaoSupport implements IStockDao {
    @Override
    public void insertStock(String sname, int amount) {
        String sql = "insert into t_stock(name, count) values (?, ?)";
        this.getJdbcTemplate().update(sql, sname, amount);
    }

    @Override
    public void updateStock(String sname, int amount, boolean isBuy) {
        String sql = "update t_stock set count = count - ? where name = ?";
        if(isBuy){
            //购买股票
            sql = "update t_stock set count = count + ? where name = ?";
        }
        this.getJdbcTemplate().update(sql, amount, sname);
    }
}
