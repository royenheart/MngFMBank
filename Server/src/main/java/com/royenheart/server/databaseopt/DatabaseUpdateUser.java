package com.royenheart.server.databaseopt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 用户信息更新
 * @author RoyenHeart
 */
public class DatabaseUpdateUser extends DataBaseUpdate {

    public DatabaseUpdateUser() {}

    /**
     * 执行更新用户信息指令
     * @param con 数据库连接
     * @param tables 数据表
     * @param keyValue 条件子句中用到，指明更新查询条件和范围
     * @param fieldWithValue 需要被更新的字段
     * @return 是否执行成功
     * @throws SQLException 数据库请求失败错误
     */
    synchronized public boolean executeSql(Connection con, String tables, HashMap<String, String> keyValue,
                                           HashMap<String, String> fieldWithValue)
            throws SQLException {
        this.con = con;
        this.tables = tables;
        this.keyValue = keyValue;
        this.fieldWithValue = fieldWithValue;

        Statement stmt = con.createStatement();
        int success = stmt.executeUpdate(super.getSql());
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }

    synchronized public boolean executeSqlNoConditionBasedOnPrevious(Connection con, String tables,
                                                                     HashMap<String, String> fieldWithValue )
            throws SQLException {
        this.con = con;
        this.tables = tables;
        this.keyValue = null;
        this.fieldWithValue = fieldWithValue;

        Statement stmt = con.createStatement();
        int success = stmt.executeUpdate(this.getSqlNoConditionBasedOnPrevious());
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }

    /**
     * sql语句更新所有用户年龄+1
     * @return 需要的sql语句
     */
    protected String getSqlNoConditionBasedOnPrevious() {
        StringBuilder sql = new StringBuilder("UPDATE " + tables + " SET ");

        /*
        获取更新的数值的键值对填入sql语句中
         */
        Iterator<String> fields = fieldWithValue.keySet().iterator();
        while (fields.hasNext()) {
            String field = fields.next();
            String value = fieldWithValue.get(field);
            sql.append(field).append("=").append(field).append("+").append("'").append(value).append("'");
            if (fields.hasNext()) {
                sql.append(",");
            }
        }
        sql.append(";");
        return String.valueOf(sql);
    }
}
