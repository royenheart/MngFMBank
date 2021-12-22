package com.royenheart.server.atomics;

import com.royenheart.server.databaseopt.DatabaseUpdateUser;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 原子操作，用户信息更新
 * @author RoyenHeart
 */
public class AtomicUpdateUser extends AtomicOperations {

    private final Connection con;
    private final String tables;
    private final HashMap<String, String> updates;
    private final String accountId;

    public AtomicUpdateUser(Connection con, String tables, String accountId, HashMap<String, String> updates) {
        this.con = con;
        this.tables = tables;
        this.accountId = accountId;
        this.updates = updates;
    }

    /**
     * 单个用户信息更新
     * @return 是否更新成功
     * @throws SQLException 数据库请求错误
     */
    public boolean update() throws SQLException {
        DatabaseUpdateUser o1 = (DatabaseUpdateUser) OPERATIONS.get("uu");
        return o1.executeSql(con, tables, new HashMap<String, String>(){
            {
                this.put("accountId", accountId);
            }
        }, updates);
    }
}
