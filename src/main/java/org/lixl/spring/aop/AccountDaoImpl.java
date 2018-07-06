package org.lixl.spring.aop;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Created by Administrator on 7/6/2018.
 */
public class AccountDaoImpl extends JdbcDaoSupport implements IAccountDao {
    @Override
    public void insertAccount(String aname, double money) {
        String sql = "insert into t_account(name, money values(?, ?)";
        this.getJdbcTemplate().update(sql, aname, money);
    }

    @Override
    public void updateAccount(String aname, double money, boolean isBuy) {
        String sql = "update t_account set money=money + ? where name = ?";
        if(isBuy){
            sql = "update t_account set money = money - ？ where name = ?";
        }
        this.getJdbcTemplate().update(sql, money, aname);
    }

}
