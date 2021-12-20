package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * 删除用户
 * @author RoyenHeart
 */
public class DatabaseDelUser extends DatabaseDelete {

    public DatabaseDelUser() {}

    synchronized public boolean executeSql(Connection con, String tables, HashMap<String, String> keyValue)
            throws SQLException {
        this.con = con;
        this.tables = tables;
        this.keyValue = keyValue;

        Statement stmt = con.createStatement();
        int success = stmt.executeUpdate(getSql());
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }

}
