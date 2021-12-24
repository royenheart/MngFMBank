package com.royenheart.server.databaseopt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 查询操作，在KeyValue中填入需要查询的条件，Fields为需要被查询的字段
 * @author RoyenHeart
 */
public class DatabaseQuery extends DatabaseSelect implements DataLimit {

    public DatabaseQuery() {}

    /**
     * 根据参数选择sql语句查询数据库
     * @param con 数据库连接
     * @param tables 数据表
     * @param fields 需要被查询的字段
     * @param keyValue 查询键值对（以=方式判断）
     * @return gson格式查询数据
     * @throws SQLException 数据库请求错误
     */
    synchronized public String executeSql(Connection con, String tables, LinkedList<String> fields,
                                          HashMap<String, String> keyValue) throws SQLException {
        this.con = con;
        this.tables = tables;
        this.fields = fields;
        this.keyValue = keyValue;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(super.getSql());
        String data = returnData(rs);
        stmt.close();
        return data;
    }

    /**
     * 根据参数选择sql语句查询数据库，带判断语句
     * @param con 数据库连接
     * @param tables 数据表
     * @param fields 需要被查询的字段
     * @param keyValue 查询键值对
     * @return gson格式查询数据
     * @throws SQLException 数据库请求错误
     */
    synchronized public String executeSqlWithConditions(Connection con, String tables, LinkedList<String> fields,
                                                        HashMap<String, String> keyValue, String link) throws SQLException {
        this.con = con;
        this.tables = tables;
        this.fields = fields;
        this.keyValue = keyValue;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(this.getSql(link));
        String data = returnData(rs);
        stmt.close();
        return data;
    }

    /**
     * 查询指定字段的所有记录
     * @param con 数据库连接
     * @param tables 使用的数据表
     * @param field 使用的字段
     * @return 数据库查询数据
     * @throws SQLException 数据库请求错误
     */
    synchronized public String executeSqlOneForAll(Connection con, String tables, String field) throws SQLException {
        this.con = con;
        this.tables = tables;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM " + tables);
        String data = returnData(rs);
        stmt.close();
        return data;
    }

    /**
     * 获取sql查询字段，各字段查询方式可指定
     * @param link 各条件判断之间的逻辑运算符
     * @return sql语句
     */
    public String getSql(String link) {
        StringBuilder sql = new StringBuilder("SELECT ");

        Iterator<String> iteratorW = fields.iterator();
        while (iteratorW.hasNext()) {
            sql.append(iteratorW.next());
            if (iteratorW.hasNext()) {
                sql.append(",");
            }
        }

        sql.append(" FROM ").append(tables).append(" where ");

        Iterator<String> iteratorF = keyValue.keySet().iterator();
        while (iteratorF.hasNext()) {
            String key = iteratorF.next();
            String value = keyValue.get(key);
            sql.append(String.format("%s%s\"%s\"", key, value.charAt(0)+value.charAt(1), value.substring(2)));
            if (iteratorF.hasNext()) {
                sql.append(" ").append(link).append(" ");
            } else {
                sql.append(" ");
            }
        }
        sql.append(";");
        return sql.toString();
    }

    /**
     * 将数据库查询数据以Json格式返回
     * @param rs 数据库查询
     * @return 查询数据
     */
    @Override
    public String returnData(ResultSet rs) {
        String result;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .create();

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            // 获取列数（从1起算）
            int columnNumber = metaData.getColumnCount();
            LinkedList<HashMap<String, String>> obj = new LinkedList<>();
            while (rs.next()) {
                HashMap<String, String> record = new HashMap<>();
                obj.add(record);

                for (int i = 1; i <= columnNumber; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value =  rs.getString(columnName);
                    record.put(columnName, value);
                }
            }
            result = gson.toJson(obj);
            rs.close();
        } catch (SQLException e) {
            System.err.println("发生错误，数据库连接异常" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
