package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 用户信息更新
 * @author RoyenHeart
 */
public class DatabaseUpdateUser extends DataBaseUpdate {

    public DatabaseUpdateUser() {}

    synchronized public boolean executeSql(Connection con, String tables, HashMap<String, String> keyValue,
                                           HashMap<String, String> fieldWithValue)
            throws SQLException {
        this.con = con;
        this.tables = tables;
        this.keyValue = keyValue;
        this.fieldWithValue = fieldWithValue;

        Statement stmt = con.createStatement();
        int success = stmt.executeUpdate(getSql());
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }
}
