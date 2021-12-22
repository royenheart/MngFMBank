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

}
