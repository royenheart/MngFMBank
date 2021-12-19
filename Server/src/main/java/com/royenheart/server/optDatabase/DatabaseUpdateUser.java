package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUpdateUser extends DataBaseUpdate {

    public DatabaseUpdateUser(Connection con, String database, String field, String fieldValue) {
        super(con, database, field, fieldValue);
    }

    synchronized public String executeSql() throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(getSql());
        String data = returnData(rs);
        stmt.close();
        return data;
    }

    @Override
    String returnData(ResultSet rs) {
        String result;
        try {
            result = rs.toString();
            rs.close();
        } catch (SQLException e) {
            System.err.println("发生错误，数据库连接异常" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
