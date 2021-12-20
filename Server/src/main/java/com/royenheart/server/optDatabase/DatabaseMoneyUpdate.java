package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 更新账户余额
 * @author RoyenHeart
 */
public class DatabaseMoneyUpdate extends DataBaseUpdate {

    public DatabaseMoneyUpdate() {}

    synchronized public boolean executeSql(Connection con, String tables, HashMap<String, String> fieldWithValue,
                                           HashMap<String, String> keyValue)
            throws SQLException {
        this.con = con;
        this.tables = tables;
        this.fieldWithValue = fieldWithValue;
        this.keyValue = keyValue;

        Statement stmt = con.createStatement();
        int success = stmt.executeUpdate(getSql());
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }

}
