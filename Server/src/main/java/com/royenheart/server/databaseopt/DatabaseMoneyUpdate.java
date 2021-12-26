package com.royenheart.server.databaseopt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

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

    synchronized public boolean executeSqlAllBasedOnPrevious(Connection con, String tables,
                                                             HashMap<String, String> fieldWithValue)
            throws SQLException {
        this.con = con;
        this.tables = tables;
        this.fieldWithValue = fieldWithValue;

        Statement stmt = con.createStatement();
        int success = stmt.executeUpdate(getSqlAllBasedOnPrevious());
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }

    /**
     * 获取不加条件判断的sql语句，且更新时以原字段的数值为基础，用于计算利息
     * @return 返回指定的sql语句
     */
    protected String getSqlAllBasedOnPrevious() {
        StringBuilder sql = new StringBuilder("UPDATE " + tables + " SET ");

        /*
        获取更新的数值的键值对填入sql语句中
         */
        Iterator<String> fields = fieldWithValue.keySet().iterator();
        while (fields.hasNext()) {
            String field = fields.next();
            String value = fieldWithValue.get(field);
            sql.append(field).append("=").append(field).append("+").
               append(field).append("*").append("'").append(value).append("'");
            if (fields.hasNext()) {
                sql.append(",");
            }
        }
        sql.append(";");
        return sql.toString();
    }

}
