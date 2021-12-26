package com.royenheart.server.atomics;

import com.royenheart.server.databaseopt.DatabaseMoneyUpdate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 原子操作，更新余额
 * @author RoyenHeart
 */
public class AtomicUpdateMoney extends AtomicOperations {

    private final Connection con;
    private final String tables;
    private final String accountId;
    private final String money;

    public AtomicUpdateMoney(Connection con, String tables, String accountId, String money) {
        this.con = con;
        this.tables = tables;
        this.accountId = accountId;
        this.money = money;
    }

    /**
     * 不指定更新范围，对数据表中所有用户的钱数进行更新，同时基于原先钱数，用于计算利息
     * @param con 数据库连接
     * @param tables 需要更新的数据表
     * @param money 更新的利率
     */
    public AtomicUpdateMoney(Connection con, String tables, String money) {
        this.con = con;
        this.tables = tables;
        this.money = money;
        this.accountId = "";
    }

    /**
     * 单个用户余额更新
     * @return 是否更新成功
     * @throws SQLException 数据库请求错误
     */
    public boolean update() throws SQLException {
        DatabaseMoneyUpdate o1 = (DatabaseMoneyUpdate) OPERATIONS.get("mu");
        return o1.executeSql(con, tables,
                new HashMap<String, String>(){{
                    this.put("money", money);}},
                new HashMap<String, String>(){{
                    this.put("accountId", accountId);}});
    }

    /**
     * 全部用户余额更新，且基于先前的值乘法运算
     * @return 是否更新成功
     * @throws SQLException 数据库请求错误
     */
    public boolean updateAllBasedOnPrevious() throws SQLException {
        DatabaseMoneyUpdate o1 = (DatabaseMoneyUpdate) OPERATIONS.get("mu");
        return o1.executeSqlAllBasedOnPrevious(con, tables,
                new HashMap<String, String>(){
                    {
                        this.put("money", money);
                    }
                });
    }

}
