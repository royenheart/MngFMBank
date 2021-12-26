package com.royenheart.server.atomics;

import com.royenheart.server.databaseopt.DatabaseDelUser;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 原子操作，删除用户
 * @author RoyenHeart
 */
public class AtomicDelUser extends AtomicOperations {

    private final Connection con;
    private final String tables;
    private final String accountId;

    public AtomicDelUser(Connection con, String tables, String accountId) {
        this.con = con;
        this.tables = tables;
        this.accountId = accountId;
    }

    /**
     * 删除单个用户
     * @return 是否删除成功
     * @throws SQLException 数据库请求错误
     */
    public boolean delete() throws SQLException {
        DatabaseDelUser o1 = (DatabaseDelUser) OPERATIONS.get("du");
        return o1.executeSql(con, tables, new HashMap<String, String>(){
            {
                this.put("accountId", accountId);
            }
        });
    }

}
