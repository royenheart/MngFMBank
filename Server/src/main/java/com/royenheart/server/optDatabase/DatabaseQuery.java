package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * 查询操作，在KeyValue中填入需要查询的条件，Fields为需要被查询的字段
 * @author RoyenHeart
 */
public class DatabaseQuery extends DatabaseSelect {

    public DatabaseQuery(Connection con, String database) {
        super(con, database);
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
        StringBuffer result = new StringBuffer();
        try {
            while (rs.next()) {
                Iterator<String> iteratorW = fields.iterator();
                while (iteratorW.hasNext()) {
                    String f = iteratorW.next();
                    result.append(f).append("=").append(rs.getString(fieldMap.get(f) + 1));
                    if (iteratorW.hasNext()) {
                        result.append(",");
                    }
                }
                result.append("\n");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("发生错误，数据库连接异常" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return result.toString();
    }
}
